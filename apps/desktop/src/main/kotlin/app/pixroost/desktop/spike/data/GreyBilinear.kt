package app.pixroost.desktop.spike.data

import java.awt.image.BufferedImage

/** The image squeezed to [width]×[height] by halving steps and one bilinear step, in luminance, row by row. */
fun BufferedImage.toGreyBilinear(width: Int, height: Int): FloatArray {
    val small = scaled(width, height)
    return FloatArray(width * height) { index -> luminance(small.getRGB(index % width, index / width)) }
}
