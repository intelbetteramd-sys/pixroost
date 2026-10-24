package app.pixroost.core.result

import kotlinx.coroutines.TimeoutCancellationException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Runs [block] and turns a thrown [Exception] into [AppResult.Failure] using [mapError].
 *
 * Only exceptions are caught. An `Error` (out of memory, a missing method, a failed assertion) is a bug or a dead
 * process, not an outcome, and propagates. A [TimeoutCancellationException] is a failure of the operation, not a
 * cancellation of the caller, so it becomes a [AppResult.Failure]. Any other [CancellationException] is rethrown:
 * swallowing it would break structured concurrency. The function is inline, so [block] may call suspend functions
 * when the caller is a suspend function.
 */
// This is the one place that maps any failure to an AppError.
@Suppress("TooGenericExceptionCaught")
inline fun <T> appCatching(mapError: (Exception) -> AppError = { UnexpectedError(it) }, block: () -> T): AppResult<T> =
    try {
        AppResult.Success(block())
    } catch (e: TimeoutCancellationException) {
        AppResult.Failure(mapError(e))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        AppResult.Failure(mapError(e))
    }
