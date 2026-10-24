package app.pixroost.core.spike.phash

/**
 * [argb] (a [width]×[height] image, row by row, packed ARGB) squeezed to [targetWidth]×[targetHeight] grey by area
 * averaging: every target pixel is the mean luminance of the source pixels its rectangle covers, edge pixels counted
 * in proportion to the covered part. Used on every platform so that one photo gives one hash everywhere.
 */
fun areaAverageGrey(
    argb: IntArray,
    width: Int,
    height: Int,
    targetWidth: Int = PhashConstants.DCT_SIZE,
    targetHeight: Int = PhashConstants.DCT_SIZE,
): FloatArray {
    require(width > 0 && height > 0) { "empty image" }
    require(argb.size >= width * height) { "pixel array is smaller than the image" }
    val columns = CellOverlaps(width, targetWidth)
    val rows = CellOverlaps(height, targetHeight)
    val sums = DoubleArray(targetWidth * targetHeight)
    val line = DoubleArray(targetWidth)
    for (y in 0 until height) {
        line.fill(0.0)
        for (x in 0 until width) {
            val slot = columns.slotOf(x)
            val value = luminance(argb[y * width + x])
            for (n in 0 until columns.counts[x]) line[columns.cells[slot + n]] += value * columns.weights[slot + n]
        }
        val rowSlot = rows.slotOf(y)
        for (n in 0 until rows.counts[y]) {
            val offset = rows.cells[rowSlot + n] * targetWidth
            val weight = rows.weights[rowSlot + n]
            for (x in 0 until targetWidth) sums[offset + x] += line[x] * weight
        }
    }
    return FloatArray(sums.size) { sums[it].toFloat() }
}
