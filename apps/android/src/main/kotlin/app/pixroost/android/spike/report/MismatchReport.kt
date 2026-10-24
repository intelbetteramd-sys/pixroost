package app.pixroost.android.spike.report

import app.pixroost.android.spike.data.SourceCategory
import app.pixroost.android.spike.run.PairSample
import java.util.Locale

/** Photos whose thumbnail is far from the original: counts by source and one anonymous line per photo. */
fun mismatchLines(pairs: List<PairSample>): List<String> {
    val bad = pairs.filter { it.distance > ReportConstants.CLOSE }
    val lines = mutableListOf("== Расхождения (превью—оригинал > 4): ${share(bad.size, pairs.size)}")
    for (category in SourceCategory.ALL) {
        val total = pairs.count { it.shape.photo.category == category }
        val mismatched = bad.count { it.shape.photo.category == category }
        if (total > 0) lines += "Источник $category: всего $total, расхождений $mismatched"
    }
    val square = { sample: PairSample -> sample.thumbShape == 0 }
    lines += "Превью квадратное: ${share(pairs.count(square), pairs.size)}; среди расхождений: " +
        share(bad.count(square), bad.size)
    val squareOriginal = { sample: PairSample -> sample.originalShape == 0 }
    lines += "Оригинал квадратный: ${share(pairs.count(squareOriginal), pairs.size)}; среди расхождений: " +
        share(bad.count(squareOriginal), bad.size)
    lines += "Строки: превью W×H; оригинал W×H; пропорции превью/оригинала; MIME; источник; КБ; расстояние; " +
        "варианты a b c d e"
    lines += bad.map { mismatchLine(it) }
    return lines
}

private fun mismatchLine(sample: PairSample): String {
    val shape = sample.shape
    val variants = sample.variants
    val thumbAspect = ratio(shape.thumbWidth, shape.thumbHeight)
    val originalAspect = ratio(shape.originalWidth, shape.originalHeight)
    val results = listOf(variants.aspect, variants.trimmed, variants.cropped, variants.legacy, variants.decoded)
        .joinToString(" ") { it.distance?.toString() ?: "—" }
    return "${shape.thumbWidth}×${shape.thumbHeight}; ${shape.originalWidth}×${shape.originalHeight}; " +
        "$thumbAspect/$originalAspect; ${shape.photo.mime}; ${shape.photo.category}; " +
        "${shape.photo.sizeBytes / ReportConstants.BYTES_IN_KB} КБ; ${sample.distance}; $results"
}

private fun ratio(width: Int, height: Int): String =
    String.format(Locale.forLanguageTag("ru"), "%.2f", width.toDouble() / height)
