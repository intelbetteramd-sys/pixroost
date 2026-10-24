package app.pixroost.android.spike.image

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import app.pixroost.android.spike.data.GalleryPhoto

/**
 * The original file decoded and upright (EXIF orientation applied), downsampled by a power of two so that the
 * longer side stays at least [targetSide] pixels; [targetSide] null decodes in full. Null when it cannot be read.
 */
fun decodeOriginal(resolver: ContentResolver, photo: GalleryPhoto, targetSide: Int?): Bitmap? = runCatching {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        // ImageDecoder applies the EXIF orientation by itself.
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(resolver, photo.uri)) { decoder, info, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.setTargetSampleSize(sampleSizeFor(maxOf(info.size.width, info.size.height), targetSide))
        }
    } else {
        decodeLegacy(resolver, photo, targetSide)
    }
}.getOrNull()

private fun decodeLegacy(resolver: ContentResolver, photo: GalleryPhoto, targetSide: Int?): Bitmap? {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    resolver.openInputStream(photo.uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
    val options = BitmapFactory.Options().apply {
        inSampleSize = sampleSizeFor(maxOf(bounds.outWidth, bounds.outHeight), targetSide)
    }
    val bitmap = resolver.openInputStream(photo.uri)?.use { BitmapFactory.decodeStream(it, null, options) }
    return bitmap?.orientedBy(readOrientation(resolver, photo.uri))
}
