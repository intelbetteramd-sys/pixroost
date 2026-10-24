package app.pixroost.android.spike.image

import android.content.ContentResolver
import android.graphics.Bitmap
import android.media.ExifInterface
import app.pixroost.android.spike.data.GalleryPhoto

/** The small thumbnail stored inside the file's EXIF block (often 160×120 or 320×240); null when there is none. */
fun readEmbeddedThumbnail(resolver: ContentResolver, photo: GalleryPhoto): Bitmap? = runCatching {
    resolver.openInputStream(photo.uri)?.use { stream ->
        ExifInterface(stream).takeIf { it.hasThumbnail() }?.thumbnailBitmap
    }
}.getOrNull()
