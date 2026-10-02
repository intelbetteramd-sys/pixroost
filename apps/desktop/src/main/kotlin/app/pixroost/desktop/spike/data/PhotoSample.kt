package app.pixroost.desktop.spike.data

/** One photo of the folder: its hashes, its shot time and the hashes of each worse copy made from it. */
data class PhotoSample(
    val original: ImageHashes,
    val shotTimeMillis: Long?,
    val variants: Map<Variant, ImageHashes>,
    val decodeMillis: Long,
    val hashMillis: Long,
)
