package app.pixroost.app.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Where the user is in the app. No UI types here, so a native shell (SwiftUI on iOS) can drive it too.
 *
 * With [AppSections.returnsToStart] (a phone) the start section is always at the bottom of the back stack and
 * back from another section returns to it ("exit through home"). The settings open on top of the section the
 * user came from, so back returns there. On a computer the stack holds the one visible section and [back]
 * does nothing: the sections are equal items of the side pane, and Esc must not switch them.
 */
class NavigationState(private val sections: AppSections, initial: AppDestination = sections.start) {
    private val _backStack = MutableStateFlow(listOf(sections.start))

    /** The back stack, oldest first; the last element is the visible destination. */
    val backStack: StateFlow<List<AppDestination>> = _backStack.asStateFlow()

    /** The visible destination. */
    val current: AppDestination get() = _backStack.value.last()

    /** The main section to highlight: the visible one, or the one under the settings. */
    val selectedPrimary: AppDestination
        get() = _backStack.value.lastOrNull { it in sections.primary } ?: sections.start

    /** Whether [back] would change anything. */
    val canGoBack: Boolean get() = _backStack.value.size > 1

    init {
        navigateTo(initial)
    }

    /** Opens [destination]. Returns `false` and does nothing if this platform has no such section. */
    fun navigateTo(destination: AppDestination): Boolean {
        val known = destination in sections.all
        if (known) _backStack.value = stackFor(destination)
        return known
    }

    /** Goes one step back. Returns `false` if there is nothing to go back to, so the caller can leave the app. */
    fun back(): Boolean {
        val handled = canGoBack
        if (handled) _backStack.value = _backStack.value.dropLast(1)
        return handled
    }

    /** The whole position as a string for saved instance state. */
    fun save(): String = _backStack.value.joinToString(SEPARATOR) { it.id }

    private fun stackFor(destination: AppDestination): List<AppDestination> = when {
        !sections.returnsToStart -> listOf(destination)
        destination == sections.start -> listOf(sections.start)
        destination in sections.primary -> listOf(sections.start, destination)
        // A secondary destination goes on top of the main section that is visible now.
        else -> _backStack.value.filter { it in sections.primary }.let { it + destination }
    }

    companion object {
        private const val SEPARATOR = ","

        /** Restores the state saved by [save]; unknown or stale ids are skipped, so the worst case is the start. */
        fun restore(sections: AppSections, saved: String?): NavigationState {
            val state = NavigationState(sections)
            saved.orEmpty().split(SEPARATOR).mapNotNull(sections::find).forEach(state::navigateTo)
            return state
        }
    }
}
