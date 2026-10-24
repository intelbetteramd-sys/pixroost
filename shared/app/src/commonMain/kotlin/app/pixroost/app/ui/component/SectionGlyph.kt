package app.pixroost.app.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import app.pixroost.app.navigation.AppDestination
import app.pixroost.app.ui.title

/** Placeholder icon: the first letter of the section name, until the design system gets real icons. */
@Composable
internal fun SectionGlyph(destination: AppDestination) {
    Text(destination.title().take(1))
}
