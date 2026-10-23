package app.pixroost.core.spike.phash

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ShotTimesTest {
    @Test
    fun parsesOffsets() {
        assertEquals(10_800, parseOffsetSeconds("+03:00"))
        assertEquals(-19_800, parseOffsetSeconds("-05:30"))
        assertEquals(0, parseOffsetSeconds("+00:00"))
        assertNull(parseOffsetSeconds("Z"))
        assertNull(parseOffsetSeconds(null))
    }

    @Test
    fun photoWithOffsetMovesToUtc() {
        assertEquals(listOf<Long?>(7_200_000L), resolveShotTimes(listOf(WallTime(10_800_000L, 3600))))
    }

    @Test
    fun photoWithoutOffsetGetsTheMostCommonOne() {
        val times = listOf(
            WallTime(10_800_000L, 10_800),
            WallTime(10_800_000L, 10_800),
            WallTime(3_600_000L, 3600),
            WallTime(10_800_000L, null),
        )
        val resolved = resolveShotTimes(times)
        assertEquals(resolved[0], resolved[3])
    }

    @Test
    fun noOffsetsAnywhereKeepsWallClockAndMissingStaysMissing() {
        assertEquals(listOf(5000L, null), resolveShotTimes(listOf(WallTime(5000L, null), null)))
    }
}
