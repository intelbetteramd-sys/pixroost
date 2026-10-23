package app.pixroost.desktop.spike.report

import app.pixroost.core.spike.phash.hammingDistance
import app.pixroost.desktop.spike.data.PhotoSample

/** Every pair of different non-blank photos within [ReportConstants.LISTED_PAIR_DISTANCE]. */
fun closePairs(all: List<PhotoSample>): List<ClosePair> {
    val kept = all.filterNot { it.isBlank() }
    val pairs = mutableListOf<ClosePair>()
    for (i in kept.indices) {
        for (j in i + 1 until kept.size) {
            val distance = hammingDistance(kept[i].original.hash, kept[j].original.hash)
            if (distance <= ReportConstants.LISTED_PAIR_DISTANCE) pairs += ClosePair(kept[i], kept[j], distance)
        }
    }
    return pairs
}
