package app.pixroost.core.time

import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

/** Test [Clock] whose time moves only when the test says so. */
class FakeClock(private var current: Instant = Instant.fromEpochMilliseconds(0)) : Clock {
    override fun now(): Instant = current

    fun advanceBy(duration: Duration) {
        current += duration
    }
}
