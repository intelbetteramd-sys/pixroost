package app.pixroost.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import app.pixroost.designsystem.theme.PixroostSpacing
import app.pixroost.designsystem.theme.PixroostTokens

/** A long-word text in a narrow column: shows where a style hyphenates and where it does not. */
@Composable
internal fun HyphenationSample(name: String, style: TextStyle, modifier: Modifier = Modifier) {
    val colors = PixroostTokens.colors
    Column(modifier, verticalArrangement = Arrangement.spacedBy(PixroostSpacing.space4)) {
        Text(name, color = colors.textSecondary, style = PixroostTokens.typography.caption)
        Text(
            CatalogConstants.LONG_WORDS,
            modifier = Modifier.width(CatalogConstants.HYPHENATION_WIDTH.dp),
            color = colors.text,
            style = style,
        )
    }
}
