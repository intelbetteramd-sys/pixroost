package app.pixroost.core.result

/** The requested file, album or account does not exist (any more). */
data class NotFoundError(override val cause: Throwable? = null) : AppError
