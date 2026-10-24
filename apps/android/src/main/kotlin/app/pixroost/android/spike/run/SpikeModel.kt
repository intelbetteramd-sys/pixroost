package app.pixroost.android.spike.run

import android.content.Context
import android.os.SystemClock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.pixroost.android.spike.data.GalleryAccess
import app.pixroost.android.spike.data.countGallery
import app.pixroost.android.spike.data.galleryAccess
import app.pixroost.android.spike.data.queryGallery
import app.pixroost.android.spike.report.checkReport
import app.pixroost.android.spike.report.deliverReport
import app.pixroost.android.spike.report.fullRunReport
import app.pixroost.android.spike.report.spikeReport
import app.pixroost.android.spike.ui.model.RunKind
import app.pixroost.android.spike.ui.model.SpikeUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Holds the screen state and runs the two checks. Lives in the activity; the activity is not recreated. */
class SpikeModel(private val context: Context) {
    private val resolver = context.contentResolver
    private val store = SnapshotStore(context)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val workers = Dispatchers.Default.limitedParallelism(RunConstants.PARALLELISM)
    private var job: Job? = null
    private var snapshot: RunSnapshot? = store.load()

    var state by mutableStateOf(SpikeUiState(previousRun = snapshot?.let(::describePrevious)))
        private set

    /** Re-reads what the user allowed; called on start, on resume and after the permission dialog. */
    fun refreshAccess() {
        val access = galleryAccess(context)
        val visible = if (access == GalleryAccess.None) 0 else countGallery(resolver)
        state = state.copy(access = access, visible = visible)
    }

    fun startCheck() = launchRun(RunKind.Check) {
        val started = SystemClock.elapsedRealtime()
        val photos = withContext(Dispatchers.IO) { queryGallery(resolver, RunConstants.CHECK_LIMIT) }
        state = state.copy(total = photos.size, done = 0, message = "Превью и оригиналы…")
        val pairs = mapPhotos(photos, workers, { done, _ -> state = state.copy(done = done) }) {
            comparePhoto(resolver, it)
        }
        val pairList = pairs.filterNotNull()
        val extra = checkExtras(resolver, photos, pairList, workers) { stage ->
            state = state.copy(message = stage)
        }
        val heicPhotos = withContext(Dispatchers.IO) {
            queryGallery(resolver, RunConstants.HEIC_LIMIT, heicOnly = true)
        }
        state = state.copy(total = heicPhotos.size, done = 0, message = "HEIC → JPEG…")
        val heic = mapPhotos(heicPhotos, workers, { done, _ -> state = state.copy(done = done) }) {
            recodePhoto(resolver, it)
        }
        val seconds = (SystemClock.elapsedRealtime() - started) / MILLIS
        val text = checkReport(photos.size, pairList, heicPhotos.size, heic.filterNotNull(), seconds, extra)
        store.saveCheckText(text)
        state = state.copy(message = text)
    }

    fun startFull() = launchRun(RunKind.Full) {
        val photos = withContext(Dispatchers.IO) { queryGallery(resolver) }
        val batteryStart = batteryPercent(context)
        val started = SystemClock.elapsedRealtime()
        var failed = 0
        var blank = 0
        var charging = isCharging(context)
        fun save(done: Int, finished: Boolean) = RunSnapshot(
            total = photos.size,
            done = done,
            failed = failed,
            blank = blank,
            elapsedMs = SystemClock.elapsedRealtime() - started,
            batteryStart = batteryStart,
            batteryNow = batteryPercent(context),
            charging = charging,
            finished = finished,
        ).also {
            snapshot = it
            store.save(it)
        }
        save(0, false)
        state = state.copy(total = photos.size, done = 0, message = "Проход по галерее…")
        val results = mapPhotos(photos, workers, { done, batch ->
            failed += batch.count { it == null }
            blank += batch.count { it == true }
            charging = charging || isCharging(context)
            save(done, false)
            state = state.copy(done = done, message = "Проход по галерее: ${fullRunReport(checkNotNull(snapshot))}")
        }) { hashThumbnailOnly(resolver, it) }
        state = state.copy(message = fullRunReport(save(results.size, true)))
    }

    fun stop() {
        job?.cancel()
    }

    /** Copies the report to the clipboard, writes it to Logcat and shows it. */
    fun report() {
        val text = spikeReport(state.access, state.visible, store.loadCheckText(), snapshot)
        deliverReport(context, text)
        state = state.copy(message = text)
    }

    @Suppress("TooGenericExceptionCaught")
    private fun launchRun(kind: RunKind, block: suspend () -> Unit) {
        if (job?.isActive == true) return
        state = state.copy(running = kind, done = 0, total = 0, message = "")
        job = scope.launch {
            try {
                block()
            } catch (cancelled: CancellationException) {
                state = state.copy(message = "Остановлено. " + partial(kind))
                throw cancelled
            } catch (failure: Exception) {
                state = state.copy(message = "Ошибка: ${failure::class.simpleName}. " + partial(kind))
            } finally {
                state = state.copy(running = null)
            }
        }
    }

    private fun partial(kind: RunKind): String =
        if (kind == RunKind.Full) snapshot?.let { fullRunReport(it) }.orEmpty() else ""

    private fun describePrevious(snapshot: RunSnapshot): String =
        (if (snapshot.finished) "Прошлый проход: " else "Прошлый проход оборван: ") +
            fullRunReport(snapshot).substringAfter('\n')

    private companion object {
        const val MILLIS = 1000L
    }
}
