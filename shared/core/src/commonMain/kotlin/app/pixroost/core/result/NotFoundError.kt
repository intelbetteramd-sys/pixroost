package app.pixroost.core.result

/** The requested file, album or account does not exist (any more). */
data class NotFoundError(override val cause: Throwable? = null) : AppError {
    // The cause text can contain file paths or URLs, so it never reaches a log line through the error.
    override fun toString(): String = "NotFoundError"
}
