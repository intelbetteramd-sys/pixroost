package app.pixroost.desktop.spike.data

/** Both hashes of one image and its width to height ratio. */
data class ImageHashes(val phash: Long, val dhash: Long, val aspect: Double)
