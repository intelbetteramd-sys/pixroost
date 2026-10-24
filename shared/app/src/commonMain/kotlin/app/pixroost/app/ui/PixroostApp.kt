package app.pixroost.app.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.pixroost.app.navigation.AppDestination
import app.pixroost.app.navigation.AppPlatform
import app.pixroost.app.navigation.AppSections
import app.pixroost.app.navigation.NavigationStyle
import app.pixroost.app.ui.component.AppNavigationHost
import app.pixroost.app.ui.component.BottomBarLayout
import app.pixroost.app.ui.component.PaneLayout
import app.pixroost.app.ui.component.RailLayout

/** The app on every platform: picks the navigation style for the window and shows the current section. */
@Composable
fun PixroostApp(sections: AppSections, platform: AppPlatform, modifier: Modifier = Modifier) {
    val state = rememberNavigationState(sections)
    val backStack by state.backStack.collectAsState()
    val current = backStack.last()
    val onSelect: (AppDestination) -> Unit = { state.navigateTo(it) }

    MaterialTheme {
        BoxWithConstraints(modifier) {
            val content: @Composable () -> Unit = { AppNavigationHost(backStack, onBack = { state.back() }) }
            when (NavigationStyle.forWindow(platform, maxWidth.value.toInt())) {
                NavigationStyle.BottomBar -> BottomBarLayout(
                    sections = sections,
                    current = current,
                    selected = state.selectedPrimary,
                    onSelect = onSelect,
                    content = content,
                )

                NavigationStyle.Rail -> RailLayout(sections, current, onSelect, content = content)

                NavigationStyle.Pane -> PaneLayout(sections, current, onSelect, content = content)
            }
        }
    }
}
