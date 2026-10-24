package app.pixroost.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.pixroost.core.AppInfo
import app.pixroost.core.platformName
import app.pixroost.designsystem.theme.PixroostSpacing
import app.pixroost.designsystem.theme.PixroostTheme
import app.pixroost.designsystem.theme.PixroostTokens

/**
 * Temporary start screen of the project skeleton (step 1.1 of the development plan).
 * The feed replaces this screen in step 3.11.
 */
@Composable
fun PixroostPlaceholder(modifier: Modifier = Modifier) {
    PixroostTheme {
        val colors = PixroostTokens.colors
        Box(modifier.fillMaxSize().background(colors.background), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(PixroostSpacing.space12),
            ) {
                LampMark(outline = colors.onLamp, modifier = Modifier.size(LAMP_SIZE.dp))
                Text(AppInfo.NAME, color = colors.text, style = PixroostTokens.typography.title)
                Text(platformName(), color = colors.textSecondary, style = PixroostTokens.typography.body)
            }
        }
    }
}

private const val LAMP_SIZE = 64
