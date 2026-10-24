package app.pixroost.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import app.pixroost.designsystem.theme.PixroostSpacing
import app.pixroost.designsystem.theme.PixroostTokens

/** One text style: its name in code and a sample set in it. */
@Composable
internal fun TypeSample(name: String, style: TextStyle, sample: String, modifier: Modifier = Modifier) {
    val colors = PixroostTokens.colors
    Column(modifier, verticalArrangement = Arrangement.spacedBy(PixroostSpacing.space4)) {
        Text(name, color = colors.textSecondary, style = PixroostTokens.typography.caption)
        Text(sample, color = colors.text, style = style)
    }
}
