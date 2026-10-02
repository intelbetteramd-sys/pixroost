package app.pixroost.desktop.spike.report

import app.pixroost.core.spike.phash.hammingDistance
import app.pixroost.core.spike.phash.sharesLshBand
import app.pixroost.desktop.spike.data.PhotoSample
import app.pixroost.desktop.spike.data.Variant

/**
 * How far each kind of worse copy lands from its original: the share of copies within each threshold, for pHash
 * and dHash, and how many pairs within the "same shot" threshold the 4-band index actually finds.
 */
fun variantLines(samples: List<PhotoSample>): List<String> = Variant.entries.map { variant ->
    val pairs = samples.mapNotNull { sample -> sample.variants[variant]?.let { sample.original to it } }
    val phash = pairs.map { (a, b) -> hammingDistance(a.phash, b.phash) }
    val dhash = pairs.map { (a, b) -> hammingDistance(a.dhash, b.dhash) }
    val close = pairs.filter { (a, b) -> hammingDistance(a.phash, b.phash) <= ReportConstants.SAME_SHOT_DISTANCE }
    val indexed = close.count { (a, b) -> sharesLshBand(a.phash, b.phash) }
    "${variant.label}: pHash ${shares(phash)}; медиана ${phash.sorted().getOrElse(phash.size / 2) { 0 }}; " +
        "индекс находит ${percent(indexed, close.size)} пар ≤ ${ReportConstants.SAME_SHOT_DISTANCE} | " +
        "dHash ${shares(dhash)}"
}

private fun shares(distances: List<Int>): String = ReportConstants.THRESHOLDS.joinToString(", ") { limit ->
    "≤ $limit: ${percent(distances.count { it <= limit }, distances.size)}"
}
