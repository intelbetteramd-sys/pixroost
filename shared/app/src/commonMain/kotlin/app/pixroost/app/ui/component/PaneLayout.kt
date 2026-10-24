package app.pixroost.app.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.pixroost.app.navigation.AppDestination
import app.pixroost.app.navigation.AppSections
import app.pixroost.app.ui.title

/** Computer layout: a permanent left pane with labelled sections and the settings at its end. */
@Composable
internal fun PaneLayout(
    sections: AppSections,
    current: AppDestination,
    onSelect: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(Modifier.width(PANE_WIDTH)) {
                sections.primary.forEach { destination ->
                    PaneItem(destination, destination == current, onSelect)
                }
                Spacer(Modifier.weight(1f))
                sections.secondary.forEach { destination ->
                    PaneItem(destination, destination == current, onSelect)
                }
            }
        },
        modifier = modifier.fillMaxSize(),
    ) {
        Box(Modifier.fillMaxSize()) { content() }
    }
}

@Composable
private fun PaneItem(destination: AppDestination, selected: Boolean, onSelect: (AppDestination) -> Unit) {
    NavigationDrawerItem(
        label = { Text(destination.title()) },
        selected = selected,
        onClick = { onSelect(destination) },
    )
}

// Width of the Windows navigation pane in the mockups (docs/design/README.md).
private val PANE_WIDTH = 248.dp
