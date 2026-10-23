package app.pixroost.desktop.spike.report

import app.pixroost.core.spike.phash.hammingDistance
import app.pixroost.core.spike.phash.sharesFiveLshBands
import app.pixroost.desktop.spike.data.PhotoSample
import app.pixroost.desktop.spike.data.Variant

/**
 * How far each kind of worse copy lands from its original: the share of copies within
 * each threshold, the median, and the share of pairs within the "same shot" threshold that the 5-band
 * index finds.
 */
fun variantLines(samples: List<PhotoSample>): List<String> = Variant.entries.map { variant ->
    val pairs = samples.mapNotNull { sample ->
        sample.variants[variant]?.let { sample.original.hash to it.hash }
    }
    val distances = pairs.map { (a, b) -> hammingDistance(a, b) }
    val close = pairs.filter { (a, b) -> hammingDistance(a, b) <= ReportConstants.SAME_SHOT_DISTANCE }
    "${variant.label}: ${shares(distances)}; медиана ${distances.sorted().getOrElse(distances.size / 2) { 0 }}; " +
        "индекс находит пар ≤ ${ReportConstants.SAME_SHOT_DISTANCE}: " +
        "5 полос ${percent(close.count { (a, b) -> sharesFiveLshBands(a, b) }, close.size)}"
}

private fun shares(distances: List<Int>): String = ReportConstants.THRESHOLDS.joinToString(", ") { limit ->
    "≤ $limit: ${percent(distances.count { it <= limit }, distances.size)}"
}
