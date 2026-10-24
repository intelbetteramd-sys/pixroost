package app.pixroost.core.result

/** A failure that no other kind describes. */
data class UnexpectedError(override val cause: Throwable? = null) : AppError
