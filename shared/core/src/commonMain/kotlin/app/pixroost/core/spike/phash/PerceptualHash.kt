package app.pixroost.core.spike.phash

import kotlin.math.PI
import kotlin.math.cos

private val dctTable: Array<DoubleArray> = Array(PhashConstants.HASH_SIZE) { u ->
    DoubleArray(PhashConstants.DCT_SIZE) { x -> cos((2 * x + 1) * u * PI / (2 * PhashConstants.DCT_SIZE)) }
}

/**
 * pHash of a 32×32 grey image given row by row: the 8×8 lowest DCT frequencies, one bit per coefficient above
 * their median. The same scheme as the `imagehash` library, so its published thresholds apply. Only the
 * low-frequency corner is computed, the rest of the DCT is not needed.
 */
fun perceptualHash(grey: FloatArray): Long {
    val size = PhashConstants.DCT_SIZE
    val low = PhashConstants.HASH_SIZE
    require(grey.size == size * size) { "expected a $size×$size image" }
    // Rows first: each row keeps its 8 lowest frequencies.
    val rows = Array(size) { y ->
        DoubleArray(low) { u -> (0 until size).sumOf { x -> grey[y * size + x] * dctTable[u][x] } }
    }
    val coefficients = DoubleArray(low * low) { index ->
        val (v, u) = index / low to index % low
        (0 until size).sumOf { y -> rows[y][u] * dctTable[v][y] }
    }
    val median = coefficients.sorted().let { (it[it.size / 2 - 1] + it[it.size / 2]) / 2 }
    return coefficients.foldIndexed(0L) { index, hash, value -> if (value > median) hash or (1L shl index) else hash }
}
