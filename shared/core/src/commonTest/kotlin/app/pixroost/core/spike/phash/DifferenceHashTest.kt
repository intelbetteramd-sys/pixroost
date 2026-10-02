package app.pixroost.core.spike.phash

import kotlin.test.Test
import kotlin.test.assertEquals

class DifferenceHashTest {
    private val width = PhashConstants.DHASH_WIDTH

    @Test
    fun setsEveryBitForBrighteningRows() {
        val gradient = FloatArray(width * PhashConstants.DHASH_HEIGHT) { (it % width).toFloat() }

        assertEquals(-1L, differenceHash(gradient))
    }

    @Test
    fun setsNoBitForDarkeningRows() {
        val gradient = FloatArray(width * PhashConstants.DHASH_HEIGHT) { (width - it % width).toFloat() }

        assertEquals(0L, differenceHash(gradient))
    }
}
