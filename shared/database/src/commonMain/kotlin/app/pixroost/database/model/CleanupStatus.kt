package app.pixroost.database.model

/**
 * State of a cleanup journal entry.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class CleanupStatus {
    PLANNED,
    DONE,
    FAILED,

    /** CleanupSafetyPolicy refused the action on the re-check. */
    REFUSED,
    RESTORED,

    /** Windows deleted the file permanently instead of moving it to the trash. */
    UNRECOVERABLE,
}
