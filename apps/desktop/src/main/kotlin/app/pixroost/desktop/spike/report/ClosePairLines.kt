package app.pixroost.desktop.spike.report

import app.pixroost.core.spike.phash.hammingDistance
import app.pixroost.desktop.spike.data.PhotoSample

/**
 * Every pair of different non-blank photos within [ReportConstants.LISTED_PAIR_DISTANCE], one anonymous line each:
 * the two numbers in the run, the distance, the gap in shot time, the proportions and both deviations. Shows whether
 * the remaining false matches are bursts of one scene.
 */
fun closePairLines(all: List<PhotoSample>): List<String> {
    val kept = all.withIndex().filterNot { it.value.isBlank() }
    val lines = mutableListOf<String>()
    for (i in kept.indices) {
        for (j in i + 1 until kept.size) {
            val (a, b) = kept[i] to kept[j]
            val distance = hammingDistance(a.value.original.hash, b.value.original.hash)
            if (distance <= ReportConstants.LISTED_PAIR_DISTANCE) lines += closePairLine(a, b, distance)
        }
    }
    return listOf("Пар разных фото с pHash ≤ ${ReportConstants.LISTED_PAIR_DISTANCE}: ${lines.size}") + lines
}

private fun closePairLine(a: IndexedValue<PhotoSample>, b: IndexedValue<PhotoSample>, distance: Int): String {
    val gap = shotGapMillis(a.value, b.value)
    val gapText = if (gap == Long.MAX_VALUE) {
        "нет даты"
    } else {
        formatRu("%.0f с", gap.toDouble() / ReportConstants.MILLIS_IN_SECOND)
    }
    val proportions = if (sameProportions(a.value.original, b.value.original)) "да" else "нет"
    return "№${a.index} и №${b.index}: расстояние $distance, разница во времени $gapText, " +
        "те же пропорции: $proportions, разброс ${formatRu("%.3f", a.value.info.grey.deviation)} и " +
        formatRu("%.3f", b.value.info.grey.deviation)
}
