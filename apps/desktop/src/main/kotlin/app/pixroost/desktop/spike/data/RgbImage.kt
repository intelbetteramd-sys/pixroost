package app.pixroost.desktop.spike.data

import java.awt.image.BufferedImage

/**
 * The image as `TYPE_INT_RGB`. The JPEG writer cannot write images with alpha (PNG) or palettes, so every image is
 * drawn into RGB before it is recoded. An image that is already RGB is returned as it is.
 */
fun BufferedImage.asRgb(): BufferedImage {
    if (type == BufferedImage.TYPE_INT_RGB) return this
    val target = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val graphics = target.createGraphics()
    graphics.drawImage(this, 0, 0, null)
    graphics.dispose()
    return target
}
