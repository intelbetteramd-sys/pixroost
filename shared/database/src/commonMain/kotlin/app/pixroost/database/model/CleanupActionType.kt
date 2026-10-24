package app.pixroost.database.model

/**
 * What a cleanup action does to the copy. Only moving to the trash exists.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class CleanupActionType {
    TRASH,
}
