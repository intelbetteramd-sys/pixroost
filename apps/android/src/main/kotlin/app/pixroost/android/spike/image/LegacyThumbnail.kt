package app.pixroost.android.spike.image

import android.content.ContentResolver
import android.graphics.Bitmap
import android.provider.MediaStore
import app.pixroost.android.spike.data.GalleryPhoto

/** The old `MediaStore.Images.Thumbnails` mini thumbnail (about 512×384); null when the system has none. */
@Suppress("DEPRECATION")
fun loadLegacyThumbnail(resolver: ContentResolver, photo: GalleryPhoto): Bitmap? = runCatching {
    MediaStore.Images.Thumbnails.getThumbnail(resolver, photo.id, MediaStore.Images.Thumbnails.MINI_KIND, null)
}.getOrNull()
