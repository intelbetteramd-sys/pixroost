package app.pixroost.core.dispatchers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlin.test.Test
import kotlin.test.assertSame

class DefaultDispatcherProviderIoTest {
    @Test
    fun ioIsTheIoDispatcher() {
        assertSame(Dispatchers.IO, DefaultDispatcherProvider.io)
    }
}
