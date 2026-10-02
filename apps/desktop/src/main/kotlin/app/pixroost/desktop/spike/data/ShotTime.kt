package app.pixroost.desktop.spike.data

import com.drew.imaging.ImageMetadataReader
import com.drew.imaging.ImageProcessingException
import com.drew.metadata.exif.ExifSubIFDDirectory
import java.io.File
import java.io.IOException

/** When the photo was taken, from EXIF `DateTimeOriginal`; `null` when the file has no such tag. */
fun shotTimeMillis(file: File): Long? = try {
    ImageMetadataReader.readMetadata(file)
        .getFirstDirectoryOfType(ExifSubIFDDirectory::class.java)
        ?.dateOriginal
        ?.time
} catch (_: ImageProcessingException) {
    null
} catch (_: IOException) {
    null
}
