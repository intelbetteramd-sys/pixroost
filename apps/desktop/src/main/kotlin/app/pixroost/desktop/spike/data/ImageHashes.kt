package app.pixroost.desktop.spike.data

/** The pHash of one image by each [HashMethod] and its width to height ratio. */
data class ImageHashes(val hashes: Map<HashMethod, Long>, val aspect: Double) {
    operator fun get(method: HashMethod): Long = hashes.getValue(method)
}
