package app.pixroost.desktop.spike.data

import app.pixroost.core.spike.phash.WallTime
import app.pixroost.core.spike.phash.parseOffsetSeconds
import com.drew.imaging.ImageMetadataReader
import com.drew.imaging.ImageProcessingException
import com.drew.metadata.exif.ExifSubIFDDirectory
import java.io.File
import java.io.IOException
import java.util.TimeZone

private val utc = TimeZone.getTimeZone("UTC")

/**
 * When the photo was taken, from EXIF `DateTimeOriginal` and, when present, `OffsetTimeOriginal`; `null` when the
 * file has no date. The wall clock is read as UTC on purpose: no zone of this computer is applied. How photos with
 * and without an offset are put on one scale is decided for the whole folder by `resolveShotTimes`.
 */
fun wallTimeOf(file: File): WallTime? = try {
    ImageMetadataReader.readMetadata(file)
        .getFirstDirectoryOfType(ExifSubIFDDirectory::class.java)
        ?.let { directory ->
            directory.getDateOriginal(utc)?.let { date ->
                WallTime(date.time, parseOffsetSeconds(directory.getString(DataConstants.TAG_OFFSET_TIME_ORIGINAL)))
            }
        }
} catch (_: ImageProcessingException) {
    null
} catch (_: IOException) {
    null
}
