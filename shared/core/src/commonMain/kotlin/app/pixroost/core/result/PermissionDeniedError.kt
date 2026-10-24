package app.pixroost.core.result

/** The user or the system denied access to a file, folder or the gallery. */
data class PermissionDeniedError(override val cause: Throwable? = null) : AppError {
    // The cause text can contain file paths or URLs, so it never reaches a log line through the error.
    override fun toString(): String = "PermissionDeniedError"
}
