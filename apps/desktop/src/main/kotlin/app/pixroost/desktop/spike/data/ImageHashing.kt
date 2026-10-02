package app.pixroost.desktop.spike.data

import app.pixroost.core.spike.phash.PhashConstants
import app.pixroost.core.spike.phash.differenceHash
import app.pixroost.core.spike.phash.perceptualHash
import java.awt.image.BufferedImage

fun hashesOf(image: BufferedImage) = ImageHashes(
    phash = perceptualHash(image.toGrey(PhashConstants.DCT_SIZE, PhashConstants.DCT_SIZE)),
    dhash = differenceHash(image.toGrey(PhashConstants.DHASH_WIDTH, PhashConstants.DHASH_HEIGHT)),
    aspect = image.width.toDouble() / image.height,
)
