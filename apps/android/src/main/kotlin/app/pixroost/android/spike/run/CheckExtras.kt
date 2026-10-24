package app.pixroost.android.spike.run

import android.content.ContentResolver
import android.os.SystemClock
import app.pixroost.android.spike.data.GalleryPhoto
import app.pixroost.android.spike.report.ReportConstants
import app.pixroost.android.spike.report.mismatchLines
import app.pixroost.android.spike.report.number
import app.pixroost.android.spike.report.variantLines
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * The report parts that explain a mismatch: sources and shapes, the candidate fixes, the EXIF thumbnails of the
 * first few mismatched photos, and the speed of the thumbnail and of decoding the original on the first photos.
 */
suspend fun checkExtras(
    resolver: ContentResolver,
    photos: List<GalleryPhoto>,
    pairs: List<PairSample>,
    dispatcher: CoroutineDispatcher,
    onStage: (String) -> Unit,
): List<String> {
    val lines = mutableListOf<String>()
    lines += mismatchLines(pairs)
    lines += variantLines(pairs)
    onStage("EXIF-миниатюры…")
    val bad = pairs.filter { it.distance > ReportConstants.CLOSE }.take(RunConstants.EXIF_SAMPLES)
    lines += "== Миниатюра внутри файла (EXIF) у ${bad.size} расходящихся фото"
    withContext(Dispatchers.IO) { bad.forEach { lines += embeddedThumbnailLine(resolver, it) } }
    val sample = photos.take(RunConstants.SPEED_SAMPLE)
    lines += "== Скорость на первых ${sample.size} фото (3 параллельно)"
    onStage("Скорость: превью…")
    lines += speedLine("только превью loadThumbnail", sample, dispatcher) { hashThumbnailOnly(resolver, it) }
    onStage("Скорость: декодирование оригинала…")
    lines += speedLine("вариант e, оригинал до ~512 px", sample, dispatcher) { hashDecodedOnly(resolver, it) }
    return lines
}

private suspend fun speedLine(
    name: String,
    photos: List<GalleryPhoto>,
    dispatcher: CoroutineDispatcher,
    transform: (GalleryPhoto) -> Boolean?,
): String {
    val started = SystemClock.elapsedRealtime()
    val results = mapPhotos(photos, dispatcher, { _, _ -> }, transform)
    val seconds = (SystemClock.elapsedRealtime() - started) / RunConstants.MILLIS_IN_SECOND
    val perSecond = if (seconds > 0) results.size / seconds else 0.0
    return "$name: ${results.size} фото за ${number(seconds)} с, ${number(perSecond)} фото/с, " +
        "не открылось ${results.count { it == null }}"
}
