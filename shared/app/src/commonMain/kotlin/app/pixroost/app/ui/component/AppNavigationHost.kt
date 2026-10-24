package app.pixroost.app.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import app.pixroost.app.navigation.AppDestination
import app.pixroost.app.ui.PlaceholderScreen

/** Shows the top of [backStack] with Navigation 3 and reports back presses and gestures to [onBack]. */
@Composable
internal fun AppNavigationHost(backStack: List<AppDestination>, onBack: () -> Unit, modifier: Modifier = Modifier) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = onBack,
        entryProvider = { destination -> NavEntry(destination) { PlaceholderScreen(destination) } },
    )
}
