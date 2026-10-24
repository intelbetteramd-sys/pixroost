package app.pixroost.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.pixroost.designsystem.theme.PixroostSpacing
import app.pixroost.designsystem.theme.PixroostTokens

/** Every step of the spacing scale as a bar of that width. */
@Composable
internal fun SpacingCatalog(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(PixroostSpacing.space16),
        verticalArrangement = Arrangement.spacedBy(PixroostSpacing.space8),
    ) {
        SpacingSample("space4", PixroostSpacing.space4)
        SpacingSample("space8", PixroostSpacing.space8)
        SpacingSample("space12", PixroostSpacing.space12)
        SpacingSample("space16", PixroostSpacing.space16)
        SpacingSample("space24", PixroostSpacing.space24)
        SpacingSample("space32", PixroostSpacing.space32)
        SpacingSample("space48", PixroostSpacing.space48)
    }
}
