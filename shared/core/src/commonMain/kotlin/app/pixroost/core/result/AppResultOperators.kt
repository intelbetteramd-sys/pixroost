package app.pixroost.core.result

/** Transforms the value of a success; a failure passes through unchanged. */
inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(value))
    is AppResult.Failure -> this
}

/** Chains an operation that can fail itself; the first failure wins. */
inline fun <T, R> AppResult<T>.flatMap(transform: (T) -> AppResult<R>): AppResult<R> = when (this) {
    is AppResult.Success -> transform(value)
    is AppResult.Failure -> this
}

/** Collapses both cases into one value. */
inline fun <T, R> AppResult<T>.fold(onSuccess: (T) -> R, onFailure: (AppError) -> R): R = when (this) {
    is AppResult.Success -> onSuccess(value)
    is AppResult.Failure -> onFailure(error)
}

/** The value of a success, or `null` for a failure. */
fun <T> AppResult<T>.getOrNull(): T? = (this as? AppResult.Success)?.value

/** The error of a failure, or `null` for a success. */
fun AppResult<*>.errorOrNull(): AppError? = (this as? AppResult.Failure)?.error
