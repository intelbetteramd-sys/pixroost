package app.pixroost.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

/**
 * The dispatchers the app uses, behind an interface so tests can replace them with one test dispatcher.
 *
 * Production code never reads `Dispatchers.*` directly; it takes a [DispatcherProvider].
 */
interface DispatcherProvider {
    /** UI thread. */
    val main: CoroutineDispatcher

    /** Blocking I/O: files, database, network. */
    val io: CoroutineDispatcher

    /** CPU-bound work such as hashing and decoding. */
    val default: CoroutineDispatcher
}
