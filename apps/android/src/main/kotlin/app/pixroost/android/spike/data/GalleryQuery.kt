package app.pixroost.android.spike.data

import android.content.ContentResolver
import android.provider.MediaStore.Images.Media

/** The newest images the app may see; on Android 14 with partial access only the photos the user picked. */
fun queryGallery(resolver: ContentResolver, limit: Int = Int.MAX_VALUE, heicOnly: Boolean = false): List<GalleryPhoto> {
    val mimeTypes = GalleryConstants.HEIC_MIME_TYPES
    val selection = if (heicOnly) "${Media.MIME_TYPE} IN (${mimeTypes.joinToString { "?" }})" else null
    val arguments = if (heicOnly) mimeTypes.toTypedArray() else null
    val cursor = resolver.query(
        Media.EXTERNAL_CONTENT_URI,
        arrayOf(Media._ID, Media.MIME_TYPE),
        selection,
        arguments,
        "${Media.DATE_ADDED} DESC, ${Media._ID} DESC",
    )
    return cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(Media._ID)
        val mimeColumn = it.getColumnIndexOrThrow(Media.MIME_TYPE)
        buildList {
            while (size < limit && it.moveToNext()) {
                add(GalleryPhoto(it.getLong(idColumn), it.getString(mimeColumn).orEmpty()))
            }
        }
    }.orEmpty()
}

/** How many images are visible to the app. */
fun countGallery(resolver: ContentResolver): Int =
    resolver.query(Media.EXTERNAL_CONTENT_URI, arrayOf(Media._ID), null, null, null)?.use { it.count } ?: 0
