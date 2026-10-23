package app.pixroost.desktop.spike.report

import app.pixroost.core.spike.phash.PhashConstants
import app.pixroost.core.spike.phash.isBlankFrame
import app.pixroost.desktop.spike.data.PhotoSample

/** Whether the photo is a blank frame by the threshold kept in the constants. */
fun PhotoSample.isBlank(): Boolean = isBlankFrame(info.grey.deviation)

/**
 * How many photos are blank frames, how many each candidate threshold would mark, and the lowest deviations of the
 * photos that stay, to show the margin to the threshold. Anonymous: numbers only.
 */
fun blankFrameLines(samples: List<PhotoSample>): List<String> {
    val deviations = samples.map { it.info.grey.deviation }
    val perThreshold = ReportConstants.BLANK_THRESHOLDS.joinToString(", ") { limit ->
        "< ${formatRu("%.2f", limit)}: ${deviations.count { isBlankFrame(it, limit) }}"
    }
    val lowest = samples.filterNot { it.isBlank() }
        .map { it.info.grey.deviation }
        .sorted()
        .take(ReportConstants.LOWEST_DEVIATIONS)
        .joinToString(", ") { formatRu("%.3f", it) }
    val blank = samples.count { it.isBlank() }
    return listOf(
        "Пустых кадров (разброс < ${PhashConstants.BLANK_DEVIATION}): $blank из ${samples.size}",
        "Сколько отметил бы порог: $perThreshold",
        "${ReportConstants.LOWEST_DEVIATIONS} наименьших разбросов среди остальных фото: $lowest",
    )
}
