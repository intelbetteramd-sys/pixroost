package app.pixroost.core.dispatchers

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DispatcherProviderTest {
    @Test
    fun testProviderRunsWorkOnTheTestScheduler() = runTest {
        val dispatchers = TestDispatcherProvider(StandardTestDispatcher(testScheduler))

        val result = withContext(dispatchers.io) { 1 + 1 }

        assertEquals(2, result)
    }

    @Test
    fun defaultProviderExposesIoAndDefault() {
        assertNotNull(DefaultDispatcherProvider.io)
        assertNotNull(DefaultDispatcherProvider.default)
    }
}
