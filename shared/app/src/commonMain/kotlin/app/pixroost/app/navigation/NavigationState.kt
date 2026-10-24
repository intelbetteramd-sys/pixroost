package app.pixroost.app.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Where the user is in the app. No UI types here, so a native shell (SwiftUI on iOS) can drive it too.
 *
 * Follows the "exit through home" pattern: the start section is always at the bottom of the back stack, and
 * back from any other section returns to it. Back from the start section is not handled: the system closes
 * the app.
 */
class NavigationState(private val sections: AppSections, initial: AppDestination = sections.start) {
    private val _current = MutableStateFlow(initial.takeIf { it in sections.all } ?: sections.start)

    /** The visible section. */
    val current: StateFlow<AppDestination> = _current.asStateFlow()

    /** The back stack that ends with the visible section, oldest first. */
    val backStack: List<AppDestination>
        get() = backStackFor(_current.value)

    /** Whether [back] would change anything. */
    val canGoBack: Boolean get() = _current.value != sections.start

    /** The back stack that ends with [destination]. */
    fun backStackFor(destination: AppDestination): List<AppDestination> =
        if (destination == sections.start) listOf(sections.start) else listOf(sections.start, destination)

    /** Opens [destination]. Returns `false` and does nothing if this platform has no such section. */
    fun navigateTo(destination: AppDestination): Boolean {
        val known = destination in sections.all
        if (known) _current.value = destination
        return known
    }

    /** Goes back to the start section. Returns `false` if already there, so the caller can leave the app. */
    fun back(): Boolean {
        val handled = canGoBack
        if (handled) _current.value = sections.start
        return handled
    }

    /** The current position as a string for saved instance state. */
    fun save(): String = _current.value.id

    companion object {
        /** Restores the state saved by [save]; an unknown or stale id opens the start section. */
        fun restore(sections: AppSections, saved: String?): NavigationState =
            NavigationState(sections, saved?.let(sections::find) ?: sections.start)
    }
}
