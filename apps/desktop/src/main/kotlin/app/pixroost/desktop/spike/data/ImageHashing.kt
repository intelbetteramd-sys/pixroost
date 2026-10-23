package app.pixroost.desktop.spike.data

import app.pixroost.core.spike.phash.PhashConstants
import app.pixroost.core.spike.phash.perceptualHash
import java.awt.image.BufferedImage

fun hashesOf(image: BufferedImage) = ImageHashes(
    hashes = mapOf(
        HashMethod.Steps to perceptualHash(image.toGreyBilinear(PhashConstants.DCT_SIZE, PhashConstants.DCT_SIZE)),
        HashMethod.Box to perceptualHash(image.toGreyBox(PhashConstants.DCT_SIZE, PhashConstants.DCT_SIZE)),
    ),
    aspect = image.width.toDouble() / image.height,
)
