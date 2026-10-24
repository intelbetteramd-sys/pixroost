package app.pixroost.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import app.pixroost.app.navigation.AppDestination

/** Stand-in for a section until its feature is built. */
@Composable
internal fun PlaceholderScreen(destination: AppDestination, modifier: Modifier = Modifier) {
    Box(
        modifier.fillMaxSize().testTag(NavigationTestTags.content(destination)),
        contentAlignment = Alignment.Center,
    ) {
        Text(sectionTitle(destination), style = MaterialTheme.typography.headlineMedium)
    }
}
