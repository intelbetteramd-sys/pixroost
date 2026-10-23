package app.pixroost.core.spike.phash

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BlankFrameTest {
    private val size = PhashConstants.DCT_SIZE * PhashConstants.DCT_SIZE

    @Test
    fun blackFrameHasNoDeviation() {
        assertEquals(0.0, greyDeviation(FloatArray(size)))
        assertTrue(isBlankFrame(greyDeviation(FloatArray(size))))
    }

    @Test
    fun flatBrightFrameIsBlank() {
        assertTrue(isBlankFrame(greyDeviation(FloatArray(size) { 200f })))
    }

    @Test
    fun halfBlackHalfWhiteHasDeviationOneHalf() {
        val grey = FloatArray(size) { if (it < size / 2) 0f else PhashConstants.GREY_MAX }
        assertEquals(0.5, greyDeviation(grey), 1e-9)
        assertFalse(isBlankFrame(greyDeviation(grey)))
    }

    @Test
    fun thresholdSeparatesNearlyFlatFromPicture() {
        assertTrue(isBlankFrame(0.019))
        assertFalse(isBlankFrame(0.02))
        assertFalse(isBlankFrame(0.019, threshold = 0.01))
    }
}
