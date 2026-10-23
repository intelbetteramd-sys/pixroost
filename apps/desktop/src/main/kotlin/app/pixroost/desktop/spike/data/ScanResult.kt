package app.pixroost.desktop.spike.data

/**
 * [found] is every decodable file of the folder, before the [DataConstants.MAX_PHOTOS] limit; [taken] is how many
 * of them were read. Photos are numbered from 1 in the order they are read; [failedNumbers] are the ones that did
 * not open.
 */
data class ScanResult(
    val samples: List<PhotoSample>,
    val found: Int,
    val taken: Int,
    val skippedHeic: Int,
    val failedNumbers: List<Int>,
    val wallMillis: Long,
) {
    val failed: Int get() = failedNumbers.size
}
