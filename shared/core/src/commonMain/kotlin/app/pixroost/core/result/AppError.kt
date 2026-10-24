package app.pixroost.core.result

/**
 * Expected failure of an operation, as a value instead of an exception.
 *
 * The subtypes are the failure kinds the UI reacts to differently; everything unclassified is [UnexpectedError].
 * [cause] is kept for logs only and must not be shown to the user. Its text may contain file paths and URLs, so
 * `toString()` of the errors omits it: log `error::class.simpleName` at INFO and above, and pass [cause] as the
 * `throwable` of a log call only at DEBUG.
 */
sealed interface AppError {
    val cause: Throwable?
}
