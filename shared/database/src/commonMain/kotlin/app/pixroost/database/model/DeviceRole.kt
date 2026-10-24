package app.pixroost.database.model

/**
 * Role of a paired device.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class DeviceRole {
    PHONE,
    DESKTOP,
}
