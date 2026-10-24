package app.pixroost.core.spike.phash

/** The centered part of a [width]×[height] image that has the proportions of [targetWidth]×[targetHeight]. */
fun centerCropBox(width: Int, height: Int, targetWidth: Int, targetHeight: Int): PixelBox {
    val wider = width.toLong() * targetHeight > height.toLong() * targetWidth
    return if (wider) {
        val cropped = maxOf(1, (height.toLong() * targetWidth / targetHeight).toInt())
        val left = (width - cropped) / 2
        PixelBox(left, 0, left + cropped, height)
    } else {
        val cropped = maxOf(1, (width.toLong() * targetHeight / targetWidth).toInt())
        val top = (height - cropped) / 2
        PixelBox(0, top, width, top + cropped)
    }
}
