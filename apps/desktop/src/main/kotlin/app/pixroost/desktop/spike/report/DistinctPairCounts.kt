package app.pixroost.desktop.spike.report

import app.pixroost.core.spike.phash.hammingDistance
import app.pixroost.core.spike.phash.sharesFiveLshBands
import app.pixroost.core.spike.phash.sharesLshBand
import app.pixroost.desktop.spike.data.PhotoSample

/** What pairs of different photos look like to the hash: close hashes, the rule, index candidates. */
class DistinctPairCounts(val photos: Int) {
    var pairs = 0
    var timedPairs = 0
    val withinDistance = IntArray(ReportConstants.FALSE_DISTANCES.size)

    /** The same, only for pairs shot within a minute of each other: these grow linearly with the library. */
    val withinDistanceBurst = IntArray(ReportConstants.FALSE_DISTANCES.size)
    var sameShotBurst = 0
    var sameShotRule = 0
    var similarByHash = 0
    var similarRule = 0
    var fourBandCandidates = 0
    var fiveBandCandidates = 0

    fun add(a: PhotoSample, b: PhotoSample) {
        pairs++
        val gap = shotGapMillis(a, b)
        if (gap != Long.MAX_VALUE) timedPairs++
        val (hashA, hashB) = a.original.hash to b.original.hash
        val distance = hammingDistance(hashA, hashB)
        ReportConstants.FALSE_DISTANCES.forEachIndexed { index, limit ->
            if (distance > limit) return@forEachIndexed
            withinDistance[index]++
            if (gap <= ReportConstants.SIMILAR_MILLIS) withinDistanceBurst[index]++
        }
        if (distance <= ReportConstants.SAME_SHOT_DISTANCE) {
            if (gap <= ReportConstants.SIMILAR_MILLIS) sameShotBurst++
            if (gap <= ReportConstants.SAME_SHOT_MILLIS && sameProportions(a.original, b.original)) sameShotRule++
        }
        if (distance <= ReportConstants.SIMILAR_DISTANCE) {
            similarByHash++
            if (gap <= ReportConstants.SIMILAR_MILLIS) similarRule++
        }
        if (sharesLshBand(hashA, hashB)) fourBandCandidates++
        if (sharesFiveLshBands(hashA, hashB)) fiveBandCandidates++
    }
}
