package app.pixroost.core.spike.phash

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LshFiveBandsTest {
    private val hash = 0x0123_4567_89AB_CDEFL

    private companion object {
        const val CHOOSE_FOUR_OF_SIXTY_FOUR = 635_376
    }

    @Test
    fun findsEveryPairAtFourBits() {
        // Every set of four bits is a lower pair and a higher pair that does not overlap it.
        val bits = PhashConstants.BITS_IN_HASH
        val pairs = (0 until bits).flatMap { low ->
            (low + 1 until bits).map { high -> Triple((1L shl low) or (1L shl high), low, high) }
        }
        var checked = 0
        for ((lowerMask, _, lowerHigh) in pairs) {
            for ((upperMask, upperLow, _) in pairs) {
                if (upperLow <= lowerHigh) continue
                assertTrue(sharesFiveLshBands(hash, hash xor (lowerMask or upperMask)))
                checked++
            }
        }
        assertEquals(CHOOSE_FOUR_OF_SIXTY_FOUR, checked)
    }

    @Test
    fun missesBitsSpreadOverAllFiveBands() {
        // One bit in each band: bits 0, 13, 26, 39 and 52.
        val flipped = (1L shl 0) or (1L shl 13) or (1L shl 26) or (1L shl 39) or (1L shl 52)

        assertFalse(sharesFiveLshBands(hash, hash xor flipped))
    }

    @Test
    fun bandWidthsCoverTheWholeHash() {
        assertEquals(PhashConstants.BITS_IN_HASH, PhashConstants.FIVE_BAND_WIDTHS.sum())
    }
}
