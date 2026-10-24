package app.pixroost.core.spike.phash

import kotlin.test.Test
import kotlin.test.assertEquals

class PixelBoxesTest {
    private val black = 0xFF000000.toInt()
    private val photo = 0xFF909090.toInt()
    private val shadow = 0xFF404040.toInt()

    private fun texture(index: Int) = if (index % 2 == 0) photo else shadow

    @Test
    fun letterboxBarsAreTrimmed() {
        val width = 10
        val height = 10
        // Two black rows on top and bottom, a textured picture between.
        val pixels = IntArray(width * height) { index ->
            val y = index / width
            if (y < 2 || y >= height - 2) black else texture(index)
        }
        assertEquals(PixelBox(0, 2, 10, 8), trimBorders(pixels, width, height))
    }

    @Test
    fun pillarboxBarsAreTrimmed() {
        val width = 10
        val height = 6
        val pixels = IntArray(width * height) { index ->
            val x = index % width
            if (x < 3 || x >= width - 3) black else texture(index)
        }
        assertEquals(PixelBox(3, 0, 7, 6), trimBorders(pixels, width, height))
    }

    @Test
    fun picturesWithoutBarsAreKept() {
        val pixels = IntArray(16) { texture(it) }
        assertEquals(PixelBox(0, 0, 4, 4), trimBorders(pixels, 4, 4))
    }

    @Test
    fun wideImageIsCroppedToSquareAtTheCenter() {
        assertEquals(PixelBox(100, 0, 300, 200), centerCropBox(400, 200, 512, 512))
    }

    @Test
    fun tallImageIsCroppedToSquareAtTheCenter() {
        assertEquals(PixelBox(0, 100, 200, 300), centerCropBox(200, 400, 1, 1))
    }

    @Test
    fun sameProportionsKeepEverything() {
        assertEquals(PixelBox(0, 0, 400, 300), centerCropBox(400, 300, 512, 384))
    }

    @Test
    fun cropTakesTheBoxPixels() {
        val pixels = IntArray(12) { it }
        assertEquals(listOf(5, 6, 9, 10), cropPixels(pixels, 4, PixelBox(1, 1, 3, 3)).toList())
    }
}
