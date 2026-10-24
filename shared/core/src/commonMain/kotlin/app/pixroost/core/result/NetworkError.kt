package app.pixroost.core.result

/** No connection, a timeout, or a server that did not answer. */
data class NetworkError(override val cause: Throwable? = null) : AppError
