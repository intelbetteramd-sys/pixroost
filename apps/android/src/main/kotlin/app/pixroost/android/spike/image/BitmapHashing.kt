package app.pixroost.android.spike.image

import android.graphics.Bitmap
import app.pixroost.core.spike.phash.areaAverageGrey
import app.pixroost.core.spike.phash.greyDeviation
import app.pixroost.core.spike.phash.perceptualHash

/** A copy that is readable by the CPU: hardware bitmaps cannot be read or drawn in software. */
fun Bitmap.software(): Bitmap? = if (config == Bitmap.Config.HARDWARE) copy(Bitmap.Config.ARGB_8888, false) else this

/** pHash of the bitmap by the shared area-averaging function, the same code the PC check runs. */
fun hashOf(bitmap: Bitmap): PhotoHash {
    val width = bitmap.width
    val height = bitmap.height
    val pixels = IntArray(width * height)
    bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
    val grey = areaAverageGrey(pixels, width, height)
    return PhotoHash(hash = perceptualHash(grey), width = width, height = height, deviation = greyDeviation(grey))
}
