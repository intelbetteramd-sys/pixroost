package app.pixroost.database.model

/**
 * What happened in a connection log entry.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class ConnectionEvent {
    PAIRED,
    CONNECTED,
    UPLOAD,
    DOWNLOAD,
    REJECTED_REVOKED,
    PAIRING_FAILED,
}
