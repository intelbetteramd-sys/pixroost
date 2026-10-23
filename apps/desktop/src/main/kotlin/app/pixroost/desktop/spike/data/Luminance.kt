package app.pixroost.desktop.spike.data

/** Luminance (0–255) of one packed RGB pixel. */
fun luminance(rgb: Int): Float {
    val red = rgb shr DataConstants.RED_SHIFT and DataConstants.BYTE_MASK
    val green = rgb shr DataConstants.GREEN_SHIFT and DataConstants.BYTE_MASK
    val blue = rgb and DataConstants.BYTE_MASK
    return red * DataConstants.RED_WEIGHT + green * DataConstants.GREEN_WEIGHT + blue * DataConstants.BLUE_WEIGHT
}
