package app.pixroost.app.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import app.pixroost.app.navigation.NavigationState
import app.pixroost.app.ui.PlaceholderScreen

/** Shows the current section with Navigation 3 and routes back presses to [state]. */
@Composable
internal fun AppNavigationHost(state: NavigationState, modifier: Modifier = Modifier) {
    val current by state.current.collectAsState()
    NavDisplay(
        backStack = state.backStackFor(current),
        modifier = modifier,
        onBack = { state.back() },
        entryProvider = { destination -> NavEntry(destination) { PlaceholderScreen(destination) } },
    )
}
