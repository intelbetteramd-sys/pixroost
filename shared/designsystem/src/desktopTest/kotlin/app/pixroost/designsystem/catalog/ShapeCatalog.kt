package app.pixroost.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.pixroost.designsystem.theme.PixroostSpacing
import app.pixroost.designsystem.theme.PixroostTokens

/** Every corner shape of the current theme. */
@Composable
internal fun ShapeCatalog(modifier: Modifier = Modifier) {
    val shapes = PixroostTokens.shapes
    Column(
        modifier = modifier.padding(PixroostSpacing.space16),
        verticalArrangement = Arrangement.spacedBy(PixroostSpacing.space12),
    ) {
        ShapeSample("small", shapes.small)
        ShapeSample("plate", shapes.plate)
        ShapeSample("card", shapes.card)
        ShapeSample("sheet", shapes.sheet)
        ShapeSample("button", shapes.button)
        ShapeSample("field", shapes.field)
        ShapeSample("round", shapes.round)
    }
}
