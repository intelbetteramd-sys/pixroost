package app.pixroost.android.spike.data

import android.content.ContentResolver
import android.provider.MediaStore

/** The newest images the app may see; on Android 14 with partial access only the photos the user picked. */
fun queryGallery(resolver: ContentResolver, limit: Int = Int.MAX_VALUE, heicOnly: Boolean = false): List<GalleryPhoto> {
    val media = MediaStore.Images.Media
    val mimeTypes = GalleryConstants.HEIC_MIME_TYPES
    val selection = if (heicOnly) "${media.MIME_TYPE} IN (${mimeTypes.joinToString { "?" }})" else null
    val arguments = if (heicOnly) mimeTypes.toTypedArray() else null
    val cursor = resolver.query(
        media.EXTERNAL_CONTENT_URI,
        arrayOf(media._ID, media.MIME_TYPE),
        selection,
        arguments,
        "${media.DATE_ADDED} DESC, ${media._ID} DESC",
    )
    return cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(media._ID)
        val mimeColumn = it.getColumnIndexOrThrow(media.MIME_TYPE)
        buildList {
            while (size < limit && it.moveToNext()) {
                add(GalleryPhoto(it.getLong(idColumn), it.getString(mimeColumn).orEmpty()))
            }
        }
    }.orEmpty()
}

/** How many images are visible to the app. */
fun countGallery(resolver: ContentResolver): Int =
    resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media._ID), null, null, null)
        ?.use { it.count } ?: 0
