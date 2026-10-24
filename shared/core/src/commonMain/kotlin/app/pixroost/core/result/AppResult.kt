package app.pixroost.core.result

/**
 * Outcome of an operation that can fail in an expected way: either a [Success] value or a [Failure] with an
 * [AppError].
 *
 * Own type instead of `kotlin.Result`: the failure is typed and exhaustive in `when`, and the type can be used
 * as a return type without compiler restrictions. Cancellation is never a [Failure]; see [appCatching].
 */
sealed interface AppResult<out T> {
    /** The operation succeeded with [value]. */
    data class Success<out T>(val value: T) : AppResult<T>

    /** The operation failed with [error]. */
    data class Failure(val error: AppError) : AppResult<Nothing>
}
