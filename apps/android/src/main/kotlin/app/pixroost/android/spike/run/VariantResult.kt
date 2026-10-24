package app.pixroost.android.spike.run

/**
 * One candidate fix on one photo: the Hamming [distance] to the reference hash (null when the variant gave nothing,
 * for example no thumbnail), the time it took and whether it changed the picture (bars trimmed).
 */
data class VariantResult(val distance: Int?, val nanos: Long, val changed: Boolean = false)
