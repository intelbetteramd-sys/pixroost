package app.pixroost.core.dispatchers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.time.Duration.Companion.hours

class DispatcherProviderTest {
    @Test
    fun testProviderRunsWorkOnTheTestScheduler() = runTest {
        val dispatchers = TestDispatcherProvider(StandardTestDispatcher(testScheduler))

        withContext(dispatchers.io) { delay(1.hours) }

        // A real dispatcher would wait an hour; the virtual clock moved instead.
        assertEquals(1.hours.inWholeMilliseconds, testScheduler.currentTime)
    }

    @Test
    fun defaultProviderUsesStandardDispatchers() {
        assertSame(Dispatchers.Default, DefaultDispatcherProvider.default)
    }
}
