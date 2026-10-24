package app.pixroost.database.model

/**
 * How a device reached the computer.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class ConnectionRoute {
    LAN,
    REMOTE,
}
