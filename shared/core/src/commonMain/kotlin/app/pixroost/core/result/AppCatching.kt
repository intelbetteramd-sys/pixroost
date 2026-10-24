package app.pixroost.core.result

import kotlin.coroutines.cancellation.CancellationException

/**
 * Runs [block] and turns a thrown exception into [AppResult.Failure] using [mapError].
 *
 * [CancellationException] is rethrown: swallowing it would break structured concurrency. The function is inline,
 * so [block] may call suspend functions when the caller is a suspend function.
 */
// This is the one place that maps any failure to an AppError.
@Suppress("TooGenericExceptionCaught")
inline fun <T> appCatching(
    mapError: (Throwable) -> AppError = { UnexpectedError(it) },
    block: () -> T,
): AppResult<T> = try {
    AppResult.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    AppResult.Failure(mapError(e))
}
