package app.pixroost.core.result

/** A failure that no other kind describes. */
data class UnexpectedError(override val cause: Throwable? = null) : AppError {
    // The cause text can contain file paths or URLs, so it never reaches a log line through the error.
    override fun toString(): String = "UnexpectedError"
}
