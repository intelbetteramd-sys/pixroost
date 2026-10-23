package app.pixroost.core.spike.phash

import kotlin.test.Test
import kotlin.test.assertEquals

class CopyPairsTest {
    @Test
    fun closePhotosGiveAllFourPairs() {
        assertEquals(4, copyCrossMatches(original = 0L to 1L, copy = 0L to 1L, limit = 4))
    }

    @Test
    fun partlyCloseFramesGiveTheClosePairsOnly() {
        // O_a is far from the other three frames, which are equal: C_a–O_b and C_a–C_b match, O_a–* do not.
        val far = 0x3FFL
        assertEquals(2, copyCrossMatches(original = 0L to far, copy = far to far, limit = 4))
    }

    @Test
    fun allowedRejectsPairs() {
        val matches = copyCrossMatches(0L to 0L, 0L to 0L, limit = 0) { aCopy, bCopy -> !aCopy && !bCopy }
        assertEquals(1, matches)
    }

    @Test
    fun farPhotosGiveNone() {
        assertEquals(0, copyCrossMatches(0L to -1L, 0L to -1L, limit = 4))
    }
}
