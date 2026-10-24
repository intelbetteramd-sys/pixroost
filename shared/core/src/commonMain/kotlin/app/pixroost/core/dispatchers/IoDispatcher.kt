package app.pixroost.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

/** The dispatcher for blocking I/O. `Dispatchers.IO` is not visible from common code on native targets. */
internal expect fun ioDispatcher(): CoroutineDispatcher
