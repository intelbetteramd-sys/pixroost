package app.pixroost.desktop.spike.report

import app.pixroost.desktop.spike.data.PhotoSample

/**
 * Every pair of different non-blank photos within [ReportConstants.LISTED_PAIR_DISTANCE], one anonymous line each:
 * the two numbers in the run, the distance, the gap in shot time, the proportions and both deviations. Shows whether
 * the remaining false matches are bursts of one scene. File names are not here, see [closePairNameLines].
 */
fun closePairLines(all: List<PhotoSample>): List<String> {
    val pairs = closePairs(all)
    return listOf("Пар разных фото с pHash ≤ ${ReportConstants.LISTED_PAIR_DISTANCE}: ${pairs.size}") +
        pairs.map { closePairLine(it) }
}

private fun closePairLine(pair: ClosePair): String {
    val (a, b) = pair.first to pair.second
    val gap = shotGapMillis(a, b)
    val gapText = if (gap == Long.MAX_VALUE) {
        "нет даты"
    } else {
        formatRu("%.0f с", gap.toDouble() / ReportConstants.MILLIS_IN_SECOND)
    }
    val proportions = if (sameProportions(a.original, b.original)) "да" else "нет"
    return "№${a.number} и №${b.number}: расстояние ${pair.distance}, разница во времени $gapText, " +
        "те же пропорции: $proportions, разброс ${formatRu("%.3f", a.info.grey.deviation)} и " +
        formatRu("%.3f", b.info.grey.deviation)
}
