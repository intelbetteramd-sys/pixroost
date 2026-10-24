package app.pixroost.core.time

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class FakeClockTest {
    @Test
    fun timeStandsStillUntilAdvanced() {
        val clock: Clock = FakeClock(Instant.fromEpochMilliseconds(1_000))

        assertEquals(clock.now(), clock.now())
    }

    @Test
    fun advanceByMovesTime() {
        val clock = FakeClock(Instant.fromEpochMilliseconds(0))

        clock.advanceBy(2.minutes)

        assertEquals(Instant.fromEpochMilliseconds(120_000), clock.now())
    }
}
