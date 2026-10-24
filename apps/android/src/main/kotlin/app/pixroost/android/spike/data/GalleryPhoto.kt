package app.pixroost.android.spike.data

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

/** One image of the gallery as MediaStore lists it. No file name is kept: the report holds numbers only. */
data class GalleryPhoto(val id: Long, val mime: String) {
    val uri: Uri
        get() = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
}
