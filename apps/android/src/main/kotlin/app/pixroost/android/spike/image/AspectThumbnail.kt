package app.pixroost.android.spike.image

import android.content.ContentResolver
import android.graphics.Bitmap
import android.os.Build
import android.util.Size
import app.pixroost.android.spike.data.GalleryPhoto

/**
 * The system thumbnail asked for at the proportions of the original: the longer side is 512 and the other one
 * follows [width]×[height]. Null before API 29 or when the file cannot be read.
 */
fun loadThumbnailAtAspect(resolver: ContentResolver, photo: GalleryPhoto, width: Int, height: Int): Bitmap? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val side = ImageConstants.THUMBNAIL_SIDE
        val size = if (width >= height) {
            Size(side, maxOf(1, side * height / width))
        } else {
            Size(maxOf(1, side * width / height), side)
        }
        runCatching { resolver.loadThumbnail(photo.uri, size, null) }.getOrNull()
    } else {
        null
    }
