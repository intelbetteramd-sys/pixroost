package app.pixroost.database.model

/**
 * Operating system of a paired device.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class DevicePlatform {
    ANDROID,
    IOS,
    MACOS,
    WINDOWS,
    LINUX,
}
