package app.pixroost.core.spike.phash

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

/**
 * How [sourceSize] source pixels cover [targetSize] target cells on one axis. Source pixel `i` covers the span
 * `[i, i + 1)` of the source, which is `[i * k, (i + 1) * k)` in target units. [weights] are the overlaps in target
 * units, so the weights of one cell add up to 1 along the axis.
 */
class CellOverlaps(sourceSize: Int, targetSize: Int) {
    private val stride = targetSize / sourceSize + 2
    val counts = IntArray(sourceSize)
    val cells = IntArray(sourceSize * stride)
    val weights = DoubleArray(sourceSize * stride)

    init {
        val k = targetSize.toDouble() / sourceSize
        for (i in 0 until sourceSize) {
            val start = i * k
            val end = (i + 1) * k
            val last = min(ceil(end).toInt(), targetSize) - 1
            for (cell in floor(start).toInt()..last) {
                val slot = i * stride + counts[i]++
                cells[slot] = cell
                weights[slot] = min(end, cell + 1.0) - max(start, cell.toDouble())
            }
        }
    }

    /** Index of the first slot of source pixel [index] in [cells] and [weights]. */
    fun slotOf(index: Int): Int = index * stride
}
