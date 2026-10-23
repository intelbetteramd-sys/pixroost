package app.pixroost.desktop.spike.data

import kotlin.math.sqrt

/** Mean and standard deviation of the luminance of a grey image, both scaled to 0–1. */
data class GreyStats(val mean: Double, val deviation: Double)

fun greyStatsOf(grey: FloatArray): GreyStats {
    val values = grey.map { it / DataConstants.GREY_MAX }
    val mean = values.average()
    return GreyStats(mean, sqrt(values.sumOf { (it - mean) * (it - mean) } / values.size))
}
