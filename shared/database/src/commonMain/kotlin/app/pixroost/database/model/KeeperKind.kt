package app.pixroost.database.model

/**
 * Where the kept copy of a cleanup action lives.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class KeeperKind {
    INSTANCE,
    ARCHIVE,
}
