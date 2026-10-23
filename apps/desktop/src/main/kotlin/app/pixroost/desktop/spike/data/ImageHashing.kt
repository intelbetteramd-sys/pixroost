package app.pixroost.desktop.spike.data

import app.pixroost.core.spike.phash.PhashConstants
import app.pixroost.core.spike.phash.perceptualHash
import java.awt.image.BufferedImage

/** The 32×32 grey image of [image] by area averaging. */
fun greyOf(image: BufferedImage): FloatArray = image.toGreyBox(PhashConstants.DCT_SIZE, PhashConstants.DCT_SIZE)

/** pHash of [image]; pass [grey] when it is already computed. */
fun hashesOf(image: BufferedImage, grey: FloatArray = greyOf(image)) =
    ImageHashes(hash = perceptualHash(grey), width = image.width, height = image.height)
