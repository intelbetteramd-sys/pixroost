package app.pixroost.android.spike.image

import android.content.ContentResolver
import android.media.ExifInterface
import android.net.Uri
import app.pixroost.core.spike.phash.needsOrientation

/** EXIF orientation of the file, 2–8 as written; 1 when the file has none, an unknown one or cannot be read. */
fun readOrientation(resolver: ContentResolver, uri: Uri): Int {
    val tag = runCatching {
        resolver.openInputStream(uri)?.use {
            ExifInterface(it).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        }
    }.getOrNull()
    return if (needsOrientation(tag)) checkNotNull(tag) else ExifInterface.ORIENTATION_NORMAL
}
