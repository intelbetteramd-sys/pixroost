package app.pixroost.android.spike.image

import android.graphics.Bitmap
import app.pixroost.core.spike.phash.areaAverageGrey
import app.pixroost.core.spike.phash.greyDeviation
import app.pixroost.core.spike.phash.perceptualHash

/** A copy that is readable by the CPU: hardware bitmaps cannot be read or drawn in software. */
fun Bitmap.software(): Bitmap? = if (config == Bitmap.Config.HARDWARE) copy(Bitmap.Config.ARGB_8888, false) else this

/** All pixels as packed ARGB, row by row. */
fun Bitmap.pixels(): IntArray {
    val result = IntArray(width * height)
    getPixels(result, 0, width, 0, 0, width, height)
    return result
}

/** pHash of the bitmap by the shared area-averaging function, the same code the PC check runs. */
fun hashOf(bitmap: Bitmap): PhotoHash = hashOfPixels(bitmap.pixels(), bitmap.width, bitmap.height)

/** pHash of ARGB pixels. */
fun hashOfPixels(pixels: IntArray, width: Int, height: Int): PhotoHash {
    val grey = areaAverageGrey(pixels, width, height)
    return PhotoHash(hash = perceptualHash(grey), width = width, height = height, deviation = greyDeviation(grey))
}
