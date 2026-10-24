package app.pixroost.database.model

/**
 * Whether a copy is the original file or a reduced one.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class InstanceQuality {
    ORIGINAL,
    REDUCED,
    UNKNOWN,
}
