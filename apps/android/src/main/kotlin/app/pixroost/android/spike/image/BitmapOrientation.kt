package app.pixroost.android.spike.image

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface

/** The bitmap turned and flipped upright by EXIF orientation [tag]; 1 and unknown tags return it as it is. */
fun Bitmap.orientedBy(tag: Int): Bitmap {
    val matrix = orientationMatrix(tag) ?: return this
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

private fun orientationMatrix(tag: Int): Matrix? = when (tag) {
    ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> Matrix().apply { setScale(ImageConstants.MIRROR_SCALE, 1f) }
    ExifInterface.ORIENTATION_ROTATE_180 -> Matrix().apply { setRotate(ImageConstants.HALF_TURN) }
    ExifInterface.ORIENTATION_FLIP_VERTICAL -> Matrix().apply {
        setRotate(ImageConstants.HALF_TURN)
        postScale(ImageConstants.MIRROR_SCALE, 1f)
    }

    ExifInterface.ORIENTATION_TRANSPOSE -> Matrix().apply {
        setRotate(ImageConstants.QUARTER_TURN)
        postScale(ImageConstants.MIRROR_SCALE, 1f)
    }

    ExifInterface.ORIENTATION_ROTATE_90 -> Matrix().apply { setRotate(ImageConstants.QUARTER_TURN) }
    ExifInterface.ORIENTATION_TRANSVERSE -> Matrix().apply {
        setRotate(-ImageConstants.QUARTER_TURN)
        postScale(ImageConstants.MIRROR_SCALE, 1f)
    }

    ExifInterface.ORIENTATION_ROTATE_270 -> Matrix().apply { setRotate(-ImageConstants.QUARTER_TURN) }
    else -> null
}
