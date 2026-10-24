package app.pixroost.android.spike.image

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import app.pixroost.core.spike.phash.orientationSwapsSides

/** Full pixel size of the file as it is shown, EXIF orientation [orientation] applied; null when unreadable. */
fun readOrientedSize(resolver: ContentResolver, uri: Uri, orientation: Int): Pair<Int, Int>? {
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    runCatching { resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, options) } }
    val valid = options.outWidth > 0 && options.outHeight > 0
    return when {
        !valid -> null
        orientationSwapsSides(orientation) -> options.outHeight to options.outWidth
        else -> options.outWidth to options.outHeight
    }
}
