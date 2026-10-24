package app.pixroost.desktop.spike.data

import app.pixroost.core.spike.phash.areaAverageGrey
import app.pixroost.core.spike.phash.perceptualHash
import java.awt.image.BufferedImage

/** The 32×32 grey image of [image] by area averaging, the shared function the phone uses too. */
fun greyOf(image: BufferedImage): FloatArray =
    areaAverageGrey(image.getRGB(0, 0, image.width, image.height, null, 0, image.width), image.width, image.height)

/** pHash of [image]; pass [grey] when it is already computed. */
fun hashesOf(image: BufferedImage, grey: FloatArray = greyOf(image)) =
    ImageHashes(hash = perceptualHash(grey), width = image.width, height = image.height)
