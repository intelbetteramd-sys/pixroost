package app.pixroost.app.ui

import app.pixroost.app.navigation.AppDestination

/** Test tags of the navigation items and section contents, so UI tests do not depend on the language. */
internal object NavigationTestTags {
    /** The navigation item that opens [destination]. */
    fun item(destination: AppDestination): String = "nav-item-${destination.id}"

    /** The content of [destination]. */
    fun content(destination: AppDestination): String = "nav-content-${destination.id}"
}
