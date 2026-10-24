package app.pixroost.core.result

/** No connection, a timeout, or a server that did not answer. */
data class NetworkError(override val cause: Throwable? = null) : AppError {
    // The cause text can contain file paths or URLs, so it never reaches a log line through the error.
    override fun toString(): String = "NetworkError"
}
