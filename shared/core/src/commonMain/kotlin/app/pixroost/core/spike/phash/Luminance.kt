package app.pixroost.core.spike.phash

/** Luminance (0–255) of one packed ARGB pixel; alpha is ignored. */
fun luminance(argb: Int): Float {
    val red = argb shr PhashConstants.RED_SHIFT and PhashConstants.BYTE_MASK
    val green = argb shr PhashConstants.GREEN_SHIFT and PhashConstants.BYTE_MASK
    val blue = argb and PhashConstants.BYTE_MASK
    return red * PhashConstants.RED_WEIGHT + green * PhashConstants.GREEN_WEIGHT + blue * PhashConstants.BLUE_WEIGHT
}
