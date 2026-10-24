package app.pixroost.database.model

/**
 * Sync state of a source.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class SourceStatus {
    OK,
    SYNCING,
    AUTH_REQUIRED,
    ERROR,
    DISABLED,
}
