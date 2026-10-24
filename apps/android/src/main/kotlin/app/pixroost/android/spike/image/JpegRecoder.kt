package app.pixroost.android.spike.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

/** The bitmap encoded to JPEG at quality 80 in memory and decoded back, as a cloud or messenger would store it. */
fun recodeToJpeg(bitmap: Bitmap): Bitmap? {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, ImageConstants.JPEG_QUALITY, bytes)
    return BitmapFactory.decodeByteArray(bytes.toByteArray(), 0, bytes.size())
}
