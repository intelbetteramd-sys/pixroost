package app.pixroost.core.result

/** The cloud rejected or revoked the sign-in; the user must sign in again. */
data class UnauthorizedError(override val cause: Throwable? = null) : AppError
