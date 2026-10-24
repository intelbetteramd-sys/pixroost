package app.pixroost.database.model

/**
 * State of a transfer job.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class TransferJobStatus {
    QUEUED,
    RUNNING,
    PAUSED,
    DONE,
    FAILED,
}
