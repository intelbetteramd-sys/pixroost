package app.pixroost.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/** Production [DispatcherProvider] backed by the standard coroutine dispatchers. */
object DefaultDispatcherProvider : DispatcherProvider {
    // Read lazily: on desktop Dispatchers.Main exists only once a UI toolkit dispatcher is on the classpath.
    override val main: CoroutineDispatcher get() = Dispatchers.Main
    override val io: CoroutineDispatcher get() = Dispatchers.IO
    override val default: CoroutineDispatcher get() = Dispatchers.Default
}
