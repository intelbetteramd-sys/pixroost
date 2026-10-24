package app.pixroost.core.result

/** Reading or writing local storage failed: database, disk, a full volume. */
data class StorageError(override val cause: Throwable? = null) : AppError {
    // The cause text can contain file paths or URLs, so it never reaches a log line through the error.
    override fun toString(): String = "StorageError"
}
