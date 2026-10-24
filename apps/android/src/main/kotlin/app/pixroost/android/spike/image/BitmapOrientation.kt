package app.pixroost.android.spike.image

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface

/** The bitmap turned and flipped upright by EXIF orientation [tag]; 1 and unknown tags return it as it is. */
fun Bitmap.orientedBy(tag: Int): Bitmap {
    val (degrees, mirrored) = when (tag) {
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> 0f to true
        ExifInterface.ORIENTATION_ROTATE_180 -> ImageConstants.HALF_TURN to false
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> ImageConstants.HALF_TURN to true
        ExifInterface.ORIENTATION_TRANSPOSE -> ImageConstants.QUARTER_TURN to true
        ExifInterface.ORIENTATION_ROTATE_90 -> ImageConstants.QUARTER_TURN to false
        ExifInterface.ORIENTATION_TRANSVERSE -> -ImageConstants.QUARTER_TURN to true
        ExifInterface.ORIENTATION_ROTATE_270 -> -ImageConstants.QUARTER_TURN to false
        else -> return this
    }
    val matrix = Matrix()
    matrix.setRotate(degrees)
    if (mirrored) matrix.postScale(ImageConstants.MIRROR_SCALE, 1f)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}
