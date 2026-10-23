package app.pixroost.desktop.spike.data

/** The pHash of one image and its size in pixels (after the EXIF orientation is applied). */
data class ImageHashes(val hash: Long, val width: Int, val height: Int)
