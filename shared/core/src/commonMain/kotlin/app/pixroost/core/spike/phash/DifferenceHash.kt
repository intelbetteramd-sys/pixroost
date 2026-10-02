package app.pixroost.core.spike.phash

/** dHash of a 9×8 grey image given row by row: one bit per pair of neighbours, set when the right one is brighter. */
fun differenceHash(grey: FloatArray): Long {
    val (width, height) = PhashConstants.DHASH_WIDTH to PhashConstants.DHASH_HEIGHT
    require(grey.size == width * height) { "expected a $width×$height image" }
    var hash = 0L
    var bit = 0
    for (y in 0 until height) {
        for (x in 0 until width - 1) {
            if (grey[y * width + x + 1] > grey[y * width + x]) hash = hash or (1L shl bit)
            bit++
        }
    }
    return hash
}
