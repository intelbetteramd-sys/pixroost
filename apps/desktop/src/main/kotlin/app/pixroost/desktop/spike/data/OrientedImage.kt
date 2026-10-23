package app.pixroost.desktop.spike.data

import app.pixroost.core.spike.phash.needsOrientation
import app.pixroost.core.spike.phash.orientationSwapsSides
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import kotlin.math.PI

/**
 * The picture as it is meant to be seen: rotated and flipped by the EXIF orientation [tag] (2–8) and drawn into an
 * RGB image. Messengers and clouds bake the rotation into the pixels, so the hash must be taken from the rotated
 * picture, and so must the worse copies. Tags 1, absent or unknown return the image as it is.
 */
fun BufferedImage.oriented(tag: Int?): BufferedImage {
    if (!needsOrientation(tag)) return this
    val swap = orientationSwapsSides(tag)
    val target = BufferedImage(if (swap) height else width, if (swap) width else height, BufferedImage.TYPE_INT_RGB)
    val graphics = target.createGraphics()
    graphics.drawImage(this, orientationTransform(tag, width, height), null)
    graphics.dispose()
    return target
}

/** The transform that puts a [width]×[height] image with orientation [tag] upright; the numbers are EXIF values. */
@Suppress("MagicNumber")
private fun orientationTransform(tag: Int?, width: Int, height: Int): AffineTransform {
    val transform = AffineTransform()
    when (tag) {
        2 -> {
            transform.scale(-1.0, 1.0)
            transform.translate(-width.toDouble(), 0.0)
        }

        3 -> {
            transform.translate(width.toDouble(), height.toDouble())
            transform.rotate(PI)
        }

        4 -> {
            transform.scale(1.0, -1.0)
            transform.translate(0.0, -height.toDouble())
        }

        5 -> {
            transform.rotate(-PI / 2)
            transform.scale(-1.0, 1.0)
        }

        6 -> {
            transform.translate(height.toDouble(), 0.0)
            transform.rotate(PI / 2)
        }

        7 -> {
            transform.scale(-1.0, 1.0)
            transform.translate(-height.toDouble(), 0.0)
            transform.translate(0.0, width.toDouble())
            transform.rotate(-PI / 2)
        }

        8 -> {
            transform.translate(0.0, width.toDouble())
            transform.rotate(-PI / 2)
        }
    }
    return transform
}
