package app.pixroost.desktop.spike.data

import com.drew.imaging.ImageMetadataReader
import com.drew.imaging.ImageProcessingException
import com.drew.metadata.exif.ExifIFD0Directory
import java.io.File
import java.io.IOException

/** The EXIF orientation tag (1–8); `null` when the file has none. */
fun exifOrientation(file: File): Int? = try {
    ImageMetadataReader.readMetadata(file)
        .getFirstDirectoryOfType(ExifIFD0Directory::class.java)
        ?.takeIf { it.containsTag(ExifIFD0Directory.TAG_ORIENTATION) }
        ?.getInteger(ExifIFD0Directory.TAG_ORIENTATION)
} catch (_: ImageProcessingException) {
    null
} catch (_: IOException) {
    null
}
