package app.pixroost.core.spike.phash

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PerceptualHashTest {
    private val size = PhashConstants.DCT_SIZE

    private fun image(pixel: (x: Int, y: Int) -> Float) = FloatArray(size * size) { pixel(it % size, it / size) }

    @Test
    fun ignoresBrightnessAndContrast() {
        val scene = image { x, y -> ((x * 7 + y * 3) % 32 + (if (x > y) 40 else 0)).toFloat() }
        val brighter = FloatArray(scene.size) { scene[it] * 1.3f + 20 }

        assertEquals(0, hammingDistance(perceptualHash(scene), perceptualHash(brighter)))
    }

    @Test
    fun tellsDifferentScenesApart() {
        val horizontal = image { _, y -> if (y < size / 2) 0f else 255f }
        val vertical = image { x, _ -> if (x < size / 2) 0f else 255f }

        assertTrue(hammingDistance(perceptualHash(horizontal), perceptualHash(vertical)) > 10)
    }
}
