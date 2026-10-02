package app.pixroost.desktop.spike.data

import java.awt.RenderingHints
import java.awt.image.BufferedImage

/**
 * Scales in halving steps with bilinear filtering, then once more to the exact size: close to area averaging and
 * much faster than `getScaledInstance`. A single bilinear step from 4000 px to 32 px would sample only a few pixels.
 */
fun BufferedImage.scaled(width: Int, height: Int): BufferedImage {
    var current = this
    while (current.width / 2 >= width && current.height / 2 >= height) {
        current = current.drawn(current.width / 2, current.height / 2)
    }
    return if (current.width == width && current.height == height) current else current.drawn(width, height)
}

private fun BufferedImage.drawn(width: Int, height: Int): BufferedImage {
    val target = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val graphics = target.createGraphics()
    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    graphics.drawImage(this, 0, 0, width, height, null)
    graphics.dispose()
    return target
}
