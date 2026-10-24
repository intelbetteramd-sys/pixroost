package app.pixroost.designsystem.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.pixroost.designsystem.theme.PixroostSpacing
import app.pixroost.designsystem.theme.PixroostTokens

/** A color sample with the role name and the value. */
@Composable
internal fun ColorSwatch(role: ColorRole, modifier: Modifier = Modifier) {
    val colors = PixroostTokens.colors
    val shape = PixroostTokens.shapes.small
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(PixroostSpacing.space12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(CatalogConstants.SWATCH_SIZE.dp)
                .clip(shape)
                .background(role.color)
                .border(CatalogConstants.SWATCH_BORDER.dp, colors.line, shape),
        )
        Column {
            Text(role.name, color = colors.text, style = PixroostTokens.typography.bodyStrong)
            Text(role.color.toHexString(), color = colors.textSecondary, style = PixroostTokens.typography.caption)
        }
    }
}
