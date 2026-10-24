package app.pixroost.database.model

/**
 * Kind of a connected source.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class SourceKind {
    DEVICE_GALLERY,
    YANDEX_DISK,
    DROPBOX,
    ONEDRIVE,
    WEBDAV,
    GOOGLE_PHOTOS_PICKER,
    GOOGLE_TAKEOUT,
    LOCAL_FOLDER,
    PIXROOST_ARCHIVE,
}
