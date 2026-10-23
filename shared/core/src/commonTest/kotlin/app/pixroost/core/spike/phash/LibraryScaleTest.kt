package app.pixroost.core.spike.phash

import kotlin.test.Test
import kotlin.test.assertEquals

class LibraryScaleTest {
    @Test
    fun linearGrowsWithLibrary() {
        assertEquals(50.0, linearScale(1, 1000, 50_000), 1e-9)
        assertEquals(0.0, linearScale(5, 0, 100), 1e-9)
    }

    @Test
    fun quadraticUsesPairCount() {
        // 1 of 10 pairs; a library of 5 photos has 10 pairs.
        assertEquals(1.0, quadraticScale(1, 10, 5), 1e-9)
        assertEquals(0.0, quadraticScale(1, 0, 5), 1e-9)
    }

    @Test
    fun ruleOfThreeScalesLinearly() {
        assertEquals(30.0, ruleOfThreeBound(1000, 10_000), 1e-9)
    }
}
