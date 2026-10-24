package app.pixroost.designsystem.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.pixroost.designsystem.theme.PixroostSpacing
import app.pixroost.designsystem.theme.PixroostTokens

/** A bar [size] wide, with the step name in code. */
@Composable
internal fun SpacingSample(name: String, size: Dp, modifier: Modifier = Modifier) {
    val colors = PixroostTokens.colors
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(PixroostSpacing.space12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.width(size).height(CatalogConstants.SPACING_BAR_HEIGHT.dp).background(colors.primary))
        Text("$name · ${size.value.toInt()} dp", color = colors.text, style = PixroostTokens.typography.caption)
    }
}
