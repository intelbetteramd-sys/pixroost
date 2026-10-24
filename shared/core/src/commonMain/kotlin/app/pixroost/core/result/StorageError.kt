package app.pixroost.core.result

/** Reading or writing local storage failed: database, disk, a full volume. */
data class StorageError(override val cause: Throwable? = null) : AppError
