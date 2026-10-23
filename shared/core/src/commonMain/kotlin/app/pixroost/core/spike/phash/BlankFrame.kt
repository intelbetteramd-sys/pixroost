package app.pixroost.core.spike.phash

import kotlin.math.sqrt

/** Standard deviation of a grey image with values 0–[PhashConstants.GREY_MAX], scaled to 0–1. */
fun greyDeviation(grey: FloatArray): Double {
    if (grey.isEmpty()) return 0.0
    val values = DoubleArray(grey.size) { grey[it] / PhashConstants.GREY_MAX.toDouble() }
    val mean = values.average()
    return sqrt(values.sumOf { (it - mean) * (it - mean) } / values.size)
}

/** Whether a grey image with this [deviation] is a blank frame, one with no picture to match by. */
fun isBlankFrame(deviation: Double, threshold: Double = PhashConstants.BLANK_DEVIATION): Boolean =
    deviation < threshold
