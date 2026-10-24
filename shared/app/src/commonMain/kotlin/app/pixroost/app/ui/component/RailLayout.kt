package app.pixroost.app.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.pixroost.app.navigation.AppDestination
import app.pixroost.app.navigation.AppSections
import app.pixroost.app.ui.title

/** Tablet layout: main sections in a rail on the side, the settings at the rail's end. */
@Composable
internal fun RailLayout(
    sections: AppSections,
    current: AppDestination,
    onSelect: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(modifier.fillMaxSize().safeDrawingPadding()) {
        NavigationRail {
            sections.primary.forEach { destination ->
                RailItem(destination, destination == current, onSelect)
            }
            Spacer(Modifier.weight(1f))
            sections.secondary.forEach { destination ->
                RailItem(destination, destination == current, onSelect)
            }
        }
        Box(Modifier.weight(1f)) { content() }
    }
}

@Composable
private fun RailItem(destination: AppDestination, selected: Boolean, onSelect: (AppDestination) -> Unit) {
    NavigationRailItem(
        selected = selected,
        onClick = { onSelect(destination) },
        icon = { SectionGlyph(destination) },
        label = { Text(destination.title()) },
    )
}
