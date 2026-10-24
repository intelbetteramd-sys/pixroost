package app.pixroost.android.spike.data

import android.content.ContentResolver
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore.Images.Media

/** The newest images the app may see; on Android 14 with partial access only the photos the user picked. */
fun queryGallery(resolver: ContentResolver, limit: Int = Int.MAX_VALUE, heicOnly: Boolean = false): List<GalleryPhoto> {
    val mimeTypes = GalleryConstants.HEIC_MIME_TYPES
    val selection = if (heicOnly) "${Media.MIME_TYPE} IN (${mimeTypes.joinToString { "?" }})" else null
    val arguments = if (heicOnly) mimeTypes.toTypedArray() else null
    val hasPath = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    val columns = mutableListOf(Media._ID, Media.MIME_TYPE, Media.SIZE, Media.BUCKET_DISPLAY_NAME)
    if (hasPath) columns += Media.RELATIVE_PATH
    val cursor = resolver.query(
        Media.EXTERNAL_CONTENT_URI,
        columns.toTypedArray(),
        selection,
        arguments,
        "${Media.DATE_ADDED} DESC, ${Media._ID} DESC",
    )
    return cursor?.use {
        buildList {
            while (size < limit && it.moveToNext()) add(it.toPhoto(hasPath))
        }
    }.orEmpty()
}

private fun Cursor.toPhoto(hasPath: Boolean): GalleryPhoto {
    val path = if (hasPath) getString(getColumnIndexOrThrow(Media.RELATIVE_PATH)) else null
    val bucket = getString(getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME))
    return GalleryPhoto(
        id = getLong(getColumnIndexOrThrow(Media._ID)),
        mime = getString(getColumnIndexOrThrow(Media.MIME_TYPE)).orEmpty(),
        sizeBytes = getLong(getColumnIndexOrThrow(Media.SIZE)),
        category = SourceCategory.of(path, bucket),
    )
}

/** How many images are visible to the app. */
fun countGallery(resolver: ContentResolver): Int =
    resolver.query(Media.EXTERNAL_CONTENT_URI, arrayOf(Media._ID), null, null, null)?.use { it.count } ?: 0
