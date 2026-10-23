package app.pixroost.core.spike.phash

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProportionsMatchTest {
    @Test
    fun exactScaleMatches() {
        assertTrue(proportionsMatch(4000, 3000, 1280, 960))
    }

    @Test
    fun panoramaPreviewRoundedBySmallerSideMatches() {
        // 10000x1000 scaled to 512 px: the short side is 51.2, rounded to 51.
        assertTrue(proportionsMatch(10_000, 1000, 512, 51))
        assertTrue(proportionsMatch(10_000, 1000, 512, 52))
    }

    @Test
    fun differentCropDoesNotMatch() {
        assertFalse(proportionsMatch(4000, 3000, 4000, 2250))
    }

    @Test
    fun rotatedPictureDoesNotMatch() {
        assertFalse(proportionsMatch(4000, 3000, 3000, 4000))
    }
}
