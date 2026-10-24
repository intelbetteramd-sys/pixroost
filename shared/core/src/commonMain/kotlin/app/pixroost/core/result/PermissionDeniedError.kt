package app.pixroost.core.result

/** The user or the system denied access to a file, folder or the gallery. */
data class PermissionDeniedError(override val cause: Throwable? = null) : AppError
