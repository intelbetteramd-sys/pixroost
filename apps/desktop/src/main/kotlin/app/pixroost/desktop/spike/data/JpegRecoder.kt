package app.pixroost.desktop.spike.data

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import kotlin.math.max
import kotlin.math.roundToInt

/** Makes the [variant] copy for real: scales, writes a JPEG with its quality and decodes it back. */
fun recode(image: BufferedImage, variant: Variant): BufferedImage {
    val longSide = max(image.width, image.height)
    val factor = variant.maxSide?.let { minOf(1.0, it.toDouble() / longSide) } ?: variant.scale
    val scaled = image.scaled(
        (image.width * factor).roundToInt().coerceAtLeast(1),
        (image.height * factor).roundToInt().coerceAtLeast(1),
    )
    val bytes = ByteArrayOutputStream()
    val writer = ImageIO.getImageWritersByFormatName("jpeg").next()
    ImageIO.createImageOutputStream(bytes).use { output ->
        writer.output = output
        val params = writer.defaultWriteParam.apply {
            compressionMode = ImageWriteParam.MODE_EXPLICIT
            compressionQuality = variant.quality
        }
        writer.write(null, IIOImage(scaled, null, null), params)
    }
    writer.dispose()
    return ImageIO.read(ByteArrayInputStream(bytes.toByteArray()))
}
