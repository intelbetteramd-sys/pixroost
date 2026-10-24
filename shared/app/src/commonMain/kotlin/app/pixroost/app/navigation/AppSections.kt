package app.pixroost.app.navigation

/**
 * The sections of the app on one platform.
 *
 * [primary] are the main sections in display order; the first one is the start destination.
 * [secondary] are reachable from navigation too, but shown apart (the settings).
 */
class AppSections(val primary: List<AppDestination>, val secondary: List<AppDestination>) {
    init {
        require(primary.isNotEmpty()) { "At least one primary section is required" }
    }

    /** Where the app starts and where back from any other section leads. */
    val start: AppDestination get() = primary.first()

    /** Every destination this platform can show. */
    val all: List<AppDestination> = primary + secondary

    /** The destination with [id], or `null` if this platform has no such section. */
    fun find(id: String): AppDestination? = all.firstOrNull { it.id == id }

    companion object {
        /** Four sections on a phone, five on a computer (the archive is a folder on this computer). */
        fun forPlatform(platform: AppPlatform): AppSections = when (platform) {
            AppPlatform.Mobile -> AppSections(
                primary = listOf(
                    AppDestination.Feed,
                    AppDestination.Storages,
                    AppDestination.Cleanup,
                    AppDestination.Devices,
                ),
                secondary = listOf(AppDestination.Settings),
            )
            AppPlatform.Desktop -> AppSections(
                primary = listOf(
                    AppDestination.Feed,
                    AppDestination.Storages,
                    AppDestination.Cleanup,
                    AppDestination.Archive,
                    AppDestination.Devices,
                ),
                secondary = listOf(AppDestination.Settings),
            )
        }
    }
}
