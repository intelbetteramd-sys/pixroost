package app.pixroost.app.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import app.pixroost.app.navigation.AppDestination
import app.pixroost.app.ui.sectionTitle

/**
 * Placeholder icon: the first letter of the section name, until the design system gets real icons.
 * Hidden from accessibility, so a screen reader says only the label.
 */
@Composable
internal fun SectionGlyph(destination: AppDestination, modifier: Modifier = Modifier) {
    Text(sectionTitle(destination).take(1), modifier.clearAndSetSemantics {})
}
