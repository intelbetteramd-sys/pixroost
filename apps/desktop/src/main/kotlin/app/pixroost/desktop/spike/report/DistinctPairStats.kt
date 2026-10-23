package app.pixroost.desktop.spike.report

import app.pixroost.desktop.spike.data.PhotoSample

/**
 * Every pair of different photos in the folder: these must not be taken for the same shot. Counts how many fall
 * within the thresholds by hash alone, how many would also pass the time and proportion checks of the rule, how
 * the false pairs scale with the library size, and how many random pairs the index would hand over as candidates.
 */
fun distinctPairLines(samples: List<PhotoSample>): List<String> {
    val counts = DistinctPairCounts()
    for (i in samples.indices) {
        for (j in i + 1 until samples.size) counts.add(samples[i], samples[j])
    }
    val same = ReportConstants.SAME_SHOT_DISTANCE
    val similar = ReportConstants.SIMILAR_DISTANCE
    return listOf(
        "Пар разных фото: ${counts.pairs}, из них с датой съёмки у обоих: ${counts.timedPairs}",
        "pHash ≤ $same: из них в пределах 60 с: ${counts.sameShotBurst}; " +
            "все условия правила «тот же кадр» (1 с и пропорции): ${counts.sameShotRule}",
        "pHash ≤ $similar: ${counts.similarByHash}; из них в пределах 60 с (правило «похожие кадры»): " +
            "${counts.similarRule}",
    ) + falsePairScaleLines(counts) + indexCostLines(counts)
}
