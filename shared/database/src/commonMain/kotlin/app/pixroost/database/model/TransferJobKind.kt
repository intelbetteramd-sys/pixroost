package app.pixroost.database.model

/**
 * Who started a transfer job.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class TransferJobKind {
    MANUAL,
    AUTO_ARCHIVE,
}
