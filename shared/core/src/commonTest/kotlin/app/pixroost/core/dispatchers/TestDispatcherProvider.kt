package app.pixroost.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

/** Test [DispatcherProvider] that runs everything on one [dispatcher], usually a `StandardTestDispatcher`. */
class TestDispatcherProvider(dispatcher: CoroutineDispatcher) : DispatcherProvider {
    override val main: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
}
