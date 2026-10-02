package app.pixroost.desktop.spike.report

import app.pixroost.desktop.spike.data.PhotoSample
import kotlin.math.abs

/** Time between two shots; [Long.MAX_VALUE] when either has no shot time, so no time rule passes. */
fun shotGapMillis(a: PhotoSample, b: PhotoSample): Long {
    val (first, second) = a.shotTimeMillis to b.shotTimeMillis
    return if (first != null && second != null) abs(first - second) else Long.MAX_VALUE
}
