package app.pixroost.core.spike.phash

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AreaAverageTest {
    private val black = 0xFF000000.toInt()
    private val white = 0xFFFFFFFF.toInt()

    @Test
    fun flatImageStaysFlat() {
        val result = areaAverageGrey(IntArray(100 * 60) { 0xFF808080.toInt() }, 100, 60, 32, 32)
        assertEquals(32 * 32, result.size)
        assertTrue(result.all { abs(it - 128f) < 0.5f })
    }

    @Test
    fun halvesAreAveragedWithoutMixing() {
        val width = 8
        val pixels = IntArray(width * 2) { if (it % width < width / 2) black else white }
        val result = areaAverageGrey(pixels, width, 2, 4, 1)
        assertEquals(0f, result[0], 0.01f)
        assertEquals(0f, result[1], 0.01f)
        assertEquals(255f, result[2], 0.01f)
        assertEquals(255f, result[3], 0.01f)
    }

    @Test
    fun partiallyCoveredPixelsAreWeighted() {
        // Three source pixels into two cells: the middle pixel is shared between them.
        val result = areaAverageGrey(intArrayOf(black, white, black), 3, 1, 2, 1)
        assertEquals(85f, result[0], 0.01f)
        assertEquals(85f, result[1], 0.01f)
    }

    @Test
    fun upscalingIsAllowed() {
        val result = areaAverageGrey(intArrayOf(white), 1, 1, 2, 2)
        assertTrue(result.all { abs(it - 255f) < 0.01f })
    }
}
