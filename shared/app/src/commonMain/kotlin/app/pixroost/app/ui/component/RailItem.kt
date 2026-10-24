package app.pixroost.app.ui.component

import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import app.pixroost.app.navigation.AppDestination
import app.pixroost.app.ui.NavigationTestTags
import app.pixroost.app.ui.sectionTitle

/** One section in the tablet rail. */
@Composable
internal fun RailItem(
    destination: AppDestination,
    selected: Boolean,
    onSelect: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRailItem(
        selected = selected,
        onClick = { onSelect(destination) },
        icon = { SectionGlyph(destination) },
        modifier = modifier.testTag(NavigationTestTags.item(destination)),
        label = { Text(sectionTitle(destination)) },
    )
}
