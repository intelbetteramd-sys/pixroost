package app.pixroost.app.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import app.pixroost.app.navigation.AppDestination
import app.pixroost.app.navigation.AppSections
import app.pixroost.app.ui.NavigationTestTags
import app.pixroost.app.ui.sectionTitle

/**
 * Phone layout: main sections in a bottom bar, the settings in a top row.
 * [selected] is the main section to highlight, which differs from [current] while the settings are open.
 */
@Composable
internal fun BottomBarLayout(
    sections: AppSections,
    current: AppDestination,
    selected: AppDestination,
    onSelect: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier.fillMaxSize().safeDrawingPadding()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(sectionTitle(current), modifier = Modifier.weight(1f).padding(start = 8.dp))
            sections.secondary.forEach { destination ->
                TextButton(
                    onClick = { onSelect(destination) },
                    modifier = Modifier.testTag(NavigationTestTags.item(destination)),
                ) { Text(sectionTitle(destination)) }
            }
        }
        Column(Modifier.weight(1f)) { content() }
        NavigationBar {
            sections.primary.forEach { destination ->
                NavigationBarItem(
                    selected = destination == selected,
                    onClick = { onSelect(destination) },
                    icon = { SectionGlyph(destination) },
                    modifier = Modifier.testTag(NavigationTestTags.item(destination)),
                    label = { Text(sectionTitle(destination)) },
                )
            }
        }
    }
}
