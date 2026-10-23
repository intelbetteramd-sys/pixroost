package app.pixroost.desktop.spike.report

import app.pixroost.core.spike.phash.hammingDistance
import app.pixroost.core.spike.phash.sharesFiveLshBands
import app.pixroost.core.spike.phash.sharesLshBand
import app.pixroost.desktop.spike.data.HashMethod
import app.pixroost.desktop.spike.data.PhotoSample
import app.pixroost.desktop.spike.data.Variant

/**
 * How far each kind of worse copy lands from its original with one hashing [method]: the share of copies within
 * each threshold, the median, and the share of pairs within the "same shot" threshold that the 4-band and the
 * 5-band index find.
 */
fun variantLines(samples: List<PhotoSample>, method: HashMethod): List<String> = Variant.entries.map { variant ->
    val pairs = samples.mapNotNull { sample ->
        sample.variants[variant]?.let { sample.original[method] to it[method] }
    }
    val distances = pairs.map { (a, b) -> hammingDistance(a, b) }
    val close = pairs.filter { (a, b) -> hammingDistance(a, b) <= ReportConstants.SAME_SHOT_DISTANCE }
    "${variant.label}: ${shares(distances)}; медиана ${distances.sorted().getOrElse(distances.size / 2) { 0 }}; " +
        "индекс находит пар ≤ ${ReportConstants.SAME_SHOT_DISTANCE}: " +
        "4 полосы ${percent(close.count { (a, b) -> sharesLshBand(a, b) }, close.size)}, " +
        "5 полос ${percent(close.count { (a, b) -> sharesFiveLshBands(a, b) }, close.size)}"
}

private fun shares(distances: List<Int>): String = ReportConstants.THRESHOLDS.joinToString(", ") { limit ->
    "≤ $limit: ${percent(distances.count { it <= limit }, distances.size)}"
}
