package app.pixroost.desktop.spike.data

import app.pixroost.core.spike.phash.PhashConstants
import app.pixroost.core.spike.phash.greyDeviation

/** Mean and standard deviation of the luminance of a grey image, both scaled to 0–1. */
data class GreyStats(val mean: Double, val deviation: Double)

fun greyStatsOf(grey: FloatArray) = GreyStats(
    mean = grey.average() / PhashConstants.GREY_MAX,
    deviation = greyDeviation(grey),
)
