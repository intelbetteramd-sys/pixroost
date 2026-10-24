package app.pixroost.designsystem.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.pixroost.designsystem.theme.PixroostSpacing
import app.pixroost.designsystem.theme.PixroostTokens

/** A square cut to [shape], with the shape's name in code. */
@Composable
internal fun ShapeSample(name: String, shape: RoundedCornerShape, modifier: Modifier = Modifier) {
    val colors = PixroostTokens.colors
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(PixroostSpacing.space12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(CatalogConstants.SHAPE_SAMPLE_SIZE.dp)
                .background(colors.tonal, shape)
                .border(CatalogConstants.SWATCH_BORDER.dp, colors.outline, shape),
        )
        Text(name, color = colors.text, style = PixroostTokens.typography.bodyStrong)
    }
}
