package app.pixroost.core.spike.phash

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExifOrientationTest {
    @Test
    fun noneAndNormalNeedNothing() {
        assertFalse(needsOrientation(null))
        assertFalse(needsOrientation(1))
        assertFalse(needsOrientation(0))
        assertFalse(needsOrientation(9))
    }

    @Test
    fun twoToEightNeedTransform() {
        assertTrue((2..8).all { needsOrientation(it) })
    }

    @Test
    fun onlyFiveToEightSwapSides() {
        assertTrue((5..8).all { orientationSwapsSides(it) })
        assertFalse(listOf(null, 1, 2, 3, 4).any { orientationSwapsSides(it) })
    }
}
