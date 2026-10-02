package app.pixroost.desktop.spike.report

import app.pixroost.core.spike.phash.hammingDistance
import app.pixroost.desktop.spike.data.PhotoSample

/**
 * Every pair of different photos in the folder: these must not be taken for the same shot. Counts how many fall
 * within the thresholds by hash alone, and how many would also pass the time and proportion checks of the rule.
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
        "pHash ≤ $same: ${counts.phashSame}; из них снято в пределах 1 с и с теми же пропорциями " +
            "(правило «тот же кадр» их склеило бы): ${counts.sameShotRule}; в пределах 60 с: ${counts.phashSameBurst}",
        "pHash ≤ $similar: ${counts.phashSimilar}; из них в пределах 60 с (правило «похожие кадры»): " +
            "${counts.similarRule}",
        "dHash ≤ $same: ${counts.dhashSame}; dHash ≤ $similar: ${counts.dhashSimilar}",
    )
}

private class DistinctPairCounts {
    var pairs = 0
    var timedPairs = 0
    var phashSame = 0
    var phashSameBurst = 0
    var sameShotRule = 0
    var phashSimilar = 0
    var similarRule = 0
    var dhashSame = 0
    var dhashSimilar = 0

    fun add(a: PhotoSample, b: PhotoSample) {
        pairs++
        val gap = shotGapMillis(a, b)
        if (gap != Long.MAX_VALUE) timedPairs++
        val phash = hammingDistance(a.original.phash, b.original.phash)
        if (phash <= ReportConstants.SAME_SHOT_DISTANCE) {
            phashSame++
            if (gap <= ReportConstants.SIMILAR_MILLIS) phashSameBurst++
            if (gap <= ReportConstants.SAME_SHOT_MILLIS && sameProportions(a.original, b.original)) sameShotRule++
        }
        if (phash <= ReportConstants.SIMILAR_DISTANCE) {
            phashSimilar++
            if (gap <= ReportConstants.SIMILAR_MILLIS) similarRule++
        }
        val dhash = hammingDistance(a.original.dhash, b.original.dhash)
        if (dhash <= ReportConstants.SAME_SHOT_DISTANCE) dhashSame++
        if (dhash <= ReportConstants.SIMILAR_DISTANCE) dhashSimilar++
    }
}
