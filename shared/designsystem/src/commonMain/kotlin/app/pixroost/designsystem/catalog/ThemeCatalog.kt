package app.pixroost.designsystem.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.pixroost.designsystem.theme.PixroostTokens

/** All tokens of the current theme on one page: colors, type scale, shapes, spacing. */
@Composable
internal fun ThemeCatalog(modifier: Modifier = Modifier) {
    Column(modifier.background(PixroostTokens.colors.background)) {
        ColorCatalog()
        TypographyCatalog()
        ShapeCatalog()
        SpacingCatalog()
    }
}
