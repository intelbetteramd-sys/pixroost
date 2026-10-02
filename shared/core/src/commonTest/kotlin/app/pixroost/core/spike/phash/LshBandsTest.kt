package app.pixroost.core.spike.phash

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LshBandsTest {
    @Test
    fun findsEveryPairUpToThreeBits() {
        // Three differing bits can spoil at most three of the four bands.
        val hash = 0x0123_4567_89AB_CDEFL

        assertTrue(sharesLshBand(hash, hash xor 0x0001_0001_0001_0000L))
    }

    @Test
    fun missesFourBitsSpreadOverAllBands() {
        val hash = 0x0123_4567_89AB_CDEFL

        assertFalse(sharesLshBand(hash, hash xor 0x0001_0001_0001_0001L))
    }
}
