package app.pixroost.android.spike.report

import android.media.ExifInterface
import app.pixroost.android.spike.run.HeicSample
import app.pixroost.android.spike.run.PairSample
import app.pixroost.core.spike.phash.orientationSwapsSides

/**
 * Text of the preview check: numbers only. [asked] photos were taken, [pairs] holds the ones that opened, [extra]
 * lines go after the orientation part.
 */
fun checkReport(
    asked: Int,
    pairs: List<PairSample>,
    heicAsked: Int,
    heic: List<HeicSample>,
    seconds: Long,
    extra: List<String> = emptyList(),
): String {
    val lines = mutableListOf("== Превью против оригинала (последние $asked фото), $seconds с")
    lines += "Обработано ${pairs.size}, не открылось ${asked - pairs.size}"
    lines += "Пустых кадров: в превью ${pairs.count { it.thumbBlank }}, в оригинале ${pairs.count { it.originalBlank }}"
    lines += "Расстояние превью-оригинал, все: " + DistanceStats.of(pairs.map { it.distance }).line()
    val solid = pairs.filter { !it.thumbBlank && !it.originalBlank }
    lines += "Без пустых кадров: " + DistanceStats.of(solid.map { it.distance }).line()
    lines += orientationLines(pairs)
    lines += extra
    lines += heicLines(heicAsked, heic)
    return lines.joinToString("\n")
}

private fun orientationLines(pairs: List<PairSample>): List<String> {
    val turned = pairs.filter { it.orientation != ExifInterface.ORIENTATION_NORMAL }
    val sides = turned.filter { orientationSwapsSides(it.orientation) && it.originalShape != 0 }
    val tags = turned.groupingBy { it.orientation }.eachCount().toSortedMap()
    return listOf(
        "== Ориентация",
        "EXIF-ориентация не 1: ${share(turned.size, pairs.size)}; по значениям: " +
            tags.entries.joinToString { "${it.key}: ${it.value}" }.ifEmpty { "нет" },
        "Из них со сменой сторон (5–8, не квадратные): ${sides.size}; превью уже повёрнуто " +
            "(пропорции как у повёрнутого оригинала): ${sides.count { it.thumbShape == it.originalShape }}, " +
            "не повёрнуто: ${sides.count { it.thumbShape != it.originalShape }}",
        "Расстояние при ориентации не 1, превью как есть: " + DistanceStats.of(turned.map { it.distance }).line(),
        "То же, превью повернули ещё раз по EXIF: " + DistanceStats.of(turned.map { it.distanceRotated }).line(),
    )
}

private fun heicLines(asked: Int, heic: List<HeicSample>): List<String> {
    val header = "== HEIC → JPEG 80 (в памяти, полный декодированный кадр с ориентацией)"
    return if (asked == 0) {
        listOf(header, "HEIC/HEIF в галерее нет")
    } else {
        listOf(
            header,
            "Взято $asked, обработано ${heic.size}, пустых кадров ${heic.count { it.blank }}",
            "Расстояние оригинал-JPEG: " + DistanceStats.of(heic.map { it.distance }).line(),
        )
    }
}
