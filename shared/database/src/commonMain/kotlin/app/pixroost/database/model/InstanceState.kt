package app.pixroost.database.model

/**
 * Whether a copy was seen in its source on the last sync.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class InstanceState {
    PRESENT,
    TRASHED,
    MISSING,
}
