package app.pixroost.core.result

/** The cloud rejected or revoked the sign-in; the user must sign in again. */
data class UnauthorizedError(override val cause: Throwable? = null) : AppError {
    // The cause text can contain file paths or URLs, so it never reaches a log line through the error.
    override fun toString(): String = "UnauthorizedError"
}
