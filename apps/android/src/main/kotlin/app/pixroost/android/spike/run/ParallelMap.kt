package app.pixroost.android.spike.run

import app.pixroost.android.spike.data.GalleryPhoto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/** Applies [transform] to every photo on [dispatcher], batch by batch; [onBatch] gets the count done so far. */
suspend fun <T> mapPhotos(
    photos: List<GalleryPhoto>,
    dispatcher: CoroutineDispatcher,
    onBatch: (done: Int, results: List<T>) -> Unit,
    transform: (GalleryPhoto) -> T,
): List<T> {
    val all = ArrayList<T>(photos.size)
    for (batch in photos.chunked(RunConstants.BATCH)) {
        val results = coroutineScope { batch.map { async(dispatcher) { transform(it) } }.awaitAll() }
        all += results
        onBatch(all.size, results)
    }
    return all
}
