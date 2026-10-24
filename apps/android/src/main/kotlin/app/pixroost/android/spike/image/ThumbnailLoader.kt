package app.pixroost.android.spike.image

import android.content.ContentResolver
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import app.pixroost.android.spike.data.GalleryPhoto

/**
 * The system thumbnail, the preview the app will hash. API 29+ has `loadThumbnail`; older versions ask the old
 * thumbnail table, which may have no entry for a photo (then null). Null also when the file cannot be read.
 */
fun loadThumbnail(resolver: ContentResolver, photo: GalleryPhoto): Bitmap? = runCatching {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val side = ImageConstants.THUMBNAIL_SIDE
        resolver.loadThumbnail(photo.uri, Size(side, side), null)
    } else {
        @Suppress("DEPRECATION")
        MediaStore.Images.Thumbnails.getThumbnail(resolver, photo.id, MediaStore.Images.Thumbnails.MINI_KIND, null)
    }
}.getOrNull()
