package app.pixroost.desktop.spike.report

import app.pixroost.desktop.spike.data.ImageHashes
import kotlin.math.abs

fun sameProportions(a: ImageHashes, b: ImageHashes): Boolean =
    abs(a.aspect - b.aspect) < ReportConstants.ASPECT_TOLERANCE
