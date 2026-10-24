package app.pixroost.app.navigation

/**
 * Typed destinations of the app. Each one is a top-level section; screens nested in a section will be added
 * next to them as the features arrive.
 *
 * [id] is stable and used to save the position across process death, so never rename an existing one.
 */
sealed interface AppDestination {
    val id: String

    /** The photo feed. */
    data object Feed : AppDestination {
        override val id: String = "feed"
    }

    /** Phone, clouds and the PC with their free space. */
    data object Storages : AppDestination {
        override val id: String = "storages"
    }

    /** Duplicates and freeing space. */
    data object Cleanup : AppDestination {
        override val id: String = "cleanup"
    }

    /** Paired phones and computers. */
    data object Devices : AppDestination {
        override val id: String = "devices"
    }

    /** The photo archive folder on this computer. Desktop only. */
    data object Archive : AppDestination {
        override val id: String = "archive"
    }

    /** App settings. Not in the main navigation: a phone opens it from the top bar. */
    data object Settings : AppDestination {
        override val id: String = "settings"
    }
}
