package app.pixroost.core.result

/**
 * Expected failure of an operation, as a value instead of an exception.
 *
 * The subtypes are the failure kinds the UI reacts to differently; everything unclassified is [UnexpectedError].
 * [cause] is kept for logs only and must not be shown to the user.
 */
sealed interface AppError {
    val cause: Throwable?
}
