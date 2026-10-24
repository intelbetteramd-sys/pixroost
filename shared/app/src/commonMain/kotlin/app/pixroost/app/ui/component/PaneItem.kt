package app.pixroost.app.ui.component

import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import app.pixroost.app.navigation.AppDestination
import app.pixroost.app.ui.NavigationTestTags
import app.pixroost.app.ui.sectionTitle

/** One section in the computer's left pane. */
@Composable
internal fun PaneItem(
    destination: AppDestination,
    selected: Boolean,
    onSelect: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationDrawerItem(
        label = { Text(sectionTitle(destination)) },
        selected = selected,
        onClick = { onSelect(destination) },
        modifier = modifier.testTag(NavigationTestTags.item(destination)),
    )
}
