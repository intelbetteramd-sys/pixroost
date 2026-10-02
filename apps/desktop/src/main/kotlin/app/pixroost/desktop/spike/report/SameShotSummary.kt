package app.pixroost.desktop.spike.report

import app.pixroost.core.spike.phash.hammingDistance
import app.pixroost.desktop.spike.data.PhotoSample

/**
 * The spike's criterion: precision of "same shot" as if every photo had one worse copy. Found copies are the true
 * positives; pairs of different photos within the threshold are the false ones. Two cases: a copy without EXIF
 * (messengers), checked by pHash alone, and a cloud copy that keeps the shot time, checked by the whole rule.
 */
fun sameShotLines(samples: List<PhotoSample>): List<String> {
    val limit = ReportConstants.SAME_SHOT_DISTANCE
    val copies = samples.flatMap { sample -> sample.variants.values.map { sample.original to it } }
    val perPhoto = copies.size.toDouble() / samples.size.coerceAtLeast(1)
    val found = copies.count { (a, b) -> hammingDistance(a.phash, b.phash) <= limit }
    val foundByRule = copies.count { (a, b) ->
        hammingDistance(a.phash, b.phash) <= limit && sameProportions(a, b)
    }
    var falseByHash = 0
    var falseByRule = 0
    for (i in samples.indices) {
        for (j in i + 1 until samples.size) {
            val (a, b) = samples[i] to samples[j]
            if (hammingDistance(a.original.phash, b.original.phash) > limit) continue
            falseByHash++
            val gap = shotGapMillis(a, b)
            if (gap <= ReportConstants.SAME_SHOT_MILLIS && sameProportions(a.original, b.original)) falseByRule++
        }
    }
    return listOf(
        precisionLine("Копия без EXIF, только pHash ≤ $limit", found / perPhoto, falseByHash, found, copies.size),
        precisionLine(
            "Копия с датой съёмки, всё правило",
            foundByRule / perPhoto,
            falseByRule,
            foundByRule,
            copies.size,
        ),
    )
}

private fun precisionLine(title: String, truePerPhoto: Double, falsePairs: Int, found: Int, copies: Int): String {
    val precision = truePerPhoto / (truePerPhoto + falsePairs).coerceAtLeast(1.0)
    return "$title: найдено копий ${percent(found, copies)}, ложных пар $falsePairs, " +
        "точность ${percent(
            (precision * ReportConstants.PRECISION_SCALE).toInt(),
            ReportConstants.PRECISION_SCALE.toInt(),
        )}"
}
