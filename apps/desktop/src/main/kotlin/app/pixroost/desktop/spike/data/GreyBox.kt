package app.pixroost.desktop.spike.data

import java.awt.image.BufferedImage

/**
 * The image squeezed to [width]×[height] by area averaging: every target pixel is the mean luminance of the source
 * pixels its rectangle covers, edge pixels counted in proportion to the covered part. Source rows are read in bulk,
 * one row at a time, so a large photo is never copied whole.
 */
fun BufferedImage.toGreyBox(width: Int, height: Int): FloatArray {
    val sourceWidth = this.width
    val sourceHeight = this.height
    val columns = CellOverlaps(sourceWidth, width)
    val rows = CellOverlaps(sourceHeight, height)
    val sums = DoubleArray(width * height)
    val pixels = IntArray(sourceWidth)
    val line = DoubleArray(width)
    for (y in 0 until sourceHeight) {
        getRGB(0, y, sourceWidth, 1, pixels, 0, sourceWidth)
        line.fill(0.0)
        for (x in 0 until sourceWidth) {
            val slot = columns.slotOf(x)
            val value = luminance(pixels[x])
            for (n in 0 until columns.counts[x]) line[columns.cells[slot + n]] += value * columns.weights[slot + n]
        }
        val rowSlot = rows.slotOf(y)
        for (n in 0 until rows.counts[y]) {
            val offset = rows.cells[rowSlot + n] * width
            val weight = rows.weights[rowSlot + n]
            for (x in 0 until width) sums[offset + x] += line[x] * weight
        }
    }
    return FloatArray(sums.size) { sums[it].toFloat() }
}
