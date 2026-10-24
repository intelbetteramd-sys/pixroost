package app.pixroost.app.navigation

/** The kind of device the app runs on; decides which sections exist and how navigation looks. */
enum class AppPlatform {
    /** Phone or tablet. */
    Mobile,

    /** Windows, Linux or macOS computer. */
    Desktop,
}
