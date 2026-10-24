package app.pixroost.database.model

/**
 * Kind of a logical media item.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class MediaType {
    PHOTO,
    VIDEO,
    LIVE_PHOTO,
}
