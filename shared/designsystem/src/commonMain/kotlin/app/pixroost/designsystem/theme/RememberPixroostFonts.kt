package app.pixroost.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import app.pixroost.designsystem.resources.Res
import app.pixroost.designsystem.resources.onest_regular
import app.pixroost.designsystem.resources.onest_semibold
import app.pixroost.designsystem.resources.unbounded_semibold
import org.jetbrains.compose.resources.Font

/**
 * Loads the bundled fonts from Compose resources. Only the weights the design uses are shipped
 * (see shared/designsystem/FONTS.md).
 */
@Composable
fun rememberPixroostFonts(): PixroostFonts {
    val onestRegular = Font(Res.font.onest_regular, FontWeight.Normal)
    val onestSemiBold = Font(Res.font.onest_semibold, FontWeight.SemiBold)
    val unboundedSemiBold = Font(Res.font.unbounded_semibold, FontWeight.SemiBold)
    return remember(onestRegular, onestSemiBold, unboundedSemiBold) {
        PixroostFonts(
            display = FontFamily(unboundedSemiBold),
            ui = FontFamily(onestRegular, onestSemiBold),
        )
    }
}
