package app.pixroost.core.spike.phash

/** A rectangle of pixels: [left] and [top] are inside, [right] and [bottom] are the first pixels outside. */
data class PixelBox(val left: Int, val top: Int, val right: Int, val bottom: Int) {
    val width: Int
        get() = right - left

    val height: Int
        get() = bottom - top
}
