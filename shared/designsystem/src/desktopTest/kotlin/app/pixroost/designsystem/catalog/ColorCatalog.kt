package app.pixroost.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.pixroost.designsystem.theme.PixroostSpacing
import app.pixroost.designsystem.theme.PixroostTokens

/** Every color role of the current theme. */
@Composable
internal fun ColorCatalog(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(PixroostSpacing.space16),
        verticalArrangement = Arrangement.spacedBy(PixroostSpacing.space8),
    ) {
        for (role in PixroostTokens.colors.roles()) {
            ColorSwatch(role)
        }
    }
}
