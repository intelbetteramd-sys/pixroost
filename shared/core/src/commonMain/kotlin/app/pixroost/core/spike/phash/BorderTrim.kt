package app.pixroost.core.spike.phash

/**
 * The picture without letterbox and pillarbox bars: edge rows and columns that are flat (luminance spread within
 * [PhashConstants.BAR_SPREAD]) and near black or near white are cut off, at most [PhashConstants.BAR_MAX_SHARE]
 * of each side. Returns the whole image when it has no bars.
 */
fun trimBorders(argb: IntArray, width: Int, height: Int): PixelBox {
    val maxRows = (height * PhashConstants.BAR_MAX_SHARE).toInt()
    val maxColumns = (width * PhashConstants.BAR_MAX_SHARE).toInt()
    var top = 0
    while (top < maxRows && isBar(argb, top * width, 1, width)) top++
    var bottom = height
    while (height - bottom < maxRows && isBar(argb, (bottom - 1) * width, 1, width)) bottom--
    var left = 0
    while (left < maxColumns && isBar(argb, top * width + left, width, bottom - top)) left++
    var right = width
    while (width - right < maxColumns && isBar(argb, top * width + right - 1, width, bottom - top)) right--
    return PixelBox(left, top, right, bottom)
}

/** Whether [count] pixels from [start] with [step] between them are one flat dark or light colour. */
private fun isBar(argb: IntArray, start: Int, step: Int, count: Int): Boolean {
    var low = PhashConstants.GREY_MAX
    var high = 0f
    for (i in 0 until count) {
        val value = luminance(argb[start + i * step])
        if (value < low) low = value
        if (value > high) high = value
    }
    val flat = high - low <= PhashConstants.BAR_SPREAD
    return flat && (high <= PhashConstants.BAR_DARK || low >= PhashConstants.BAR_LIGHT)
}
