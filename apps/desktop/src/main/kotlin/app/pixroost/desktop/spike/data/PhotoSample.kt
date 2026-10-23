package app.pixroost.desktop.spike.data

import app.pixroost.core.spike.phash.WallTime

/**
 * One photo of the folder: its [number] in the order of reading (from 1), its [name] (shown on the screen only,
 * never in the copied report), what it is, its hashes, its shot time and the hashes of each worse copy of it.
 * [shotTimeMillis] is filled once the whole folder is read, see `resolveShotTimes`.
 */
data class PhotoSample(
    val number: Int,
    val name: String,
    val info: PhotoInfo,
    val original: ImageHashes,
    val wallTime: WallTime?,
    val shotTimeMillis: Long?,
    val variants: Map<Variant, ImageHashes>,
    val decodeMillis: Long,
    val hashMillis: Long,
)
