package app.pixroost.core.spike.phash

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Whether two images have the same proportions. The aspect ratios are compared by their relative difference with a
 * tolerance of [PhashConstants.PROPORTION_TOLERANCE] plus one pixel of the smaller side of both images: a copy
 * scaled to a given long side rounds its short side to whole pixels, and for a panorama in a 512 px preview that
 * rounding alone is about 2 %.
 */
fun proportionsMatch(widthA: Int, heightA: Int, widthB: Int, heightB: Int): Boolean {
    val smallerSide = min(min(widthA, heightA), min(widthB, heightB)).coerceAtLeast(1)
    val aspectA = widthA.toDouble() / heightA.coerceAtLeast(1)
    val aspectB = widthB.toDouble() / heightB.coerceAtLeast(1)
    val largest = max(aspectA, aspectB)
    if (largest == 0.0) return true
    return abs(aspectA - aspectB) / largest <= PhashConstants.PROPORTION_TOLERANCE + 1.0 / smallerSide
}
