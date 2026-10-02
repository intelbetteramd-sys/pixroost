package app.pixroost.desktop.spike.data

import java.awt.image.BufferedImage

/** The image squeezed to [width]×[height] (aspect ignored, as the hashes expect) in luminance, row by row. */
fun BufferedImage.toGrey(width: Int, height: Int): FloatArray {
    val small = scaled(width, height)
    return FloatArray(width * height) { index ->
        val rgb = small.getRGB(index % width, index / width)
        val red = rgb shr DataConstants.RED_SHIFT and DataConstants.BYTE_MASK
        val green = rgb shr DataConstants.GREEN_SHIFT and DataConstants.BYTE_MASK
        val blue = rgb and DataConstants.BYTE_MASK
        red * DataConstants.RED_WEIGHT + green * DataConstants.GREEN_WEIGHT + blue * DataConstants.BLUE_WEIGHT
    }
}
