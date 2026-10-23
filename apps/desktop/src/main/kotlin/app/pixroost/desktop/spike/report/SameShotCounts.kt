package app.pixroost.desktop.spike.report

import app.pixroost.core.spike.phash.copyCrossMatches
import app.pixroost.core.spike.phash.hammingDistance
import app.pixroost.desktop.spike.data.PhotoSample
import app.pixroost.desktop.spike.data.Variant

/**
 * Counts for the precision of "same shot", summed over the kinds of copy; divide by the number of kinds to average.
 * Found copies are original–copy pairs of one photo. False pairs are pairs of frames of two different photos:
 * with one copy per photo each close pair of originals also gives original–copy and copy–copy pairs, so all four
 * are counted. For the full rule the copy takes its original's shot time and has its own proportions.
 */
class SameShotCounts {
    var foundByHash = 0
    var foundByRule = 0
    var falseByHash = 0
    var falseByRule = 0

    fun addCopies(sample: PhotoSample) {
        for (copy in sample.variants.values) {
            if (hammingDistance(sample.original.hash, copy.hash) > ReportConstants.SAME_SHOT_DISTANCE) continue
            foundByHash++
            if (sameProportions(sample.original, copy)) foundByRule++
        }
    }

    fun addPair(a: PhotoSample, b: PhotoSample) {
        val limit = ReportConstants.SAME_SHOT_DISTANCE
        val sameTime = shotGapMillis(a, b) <= ReportConstants.SAME_SHOT_MILLIS
        for (variant in Variant.entries) {
            val (copyA, copyB) = a.variants.getValue(variant) to b.variants.getValue(variant)
            val originals = a.original.hash to b.original.hash
            val copies = copyA.hash to copyB.hash
            falseByHash += copyCrossMatches(originals, copies, limit)
            if (sameTime) {
                falseByRule += copyCrossMatches(originals, copies, limit) { aIsCopy, bIsCopy ->
                    sameProportions(if (aIsCopy) copyA else a.original, if (bIsCopy) copyB else b.original)
                }
            }
        }
    }
}
