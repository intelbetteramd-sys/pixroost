package app.pixroost.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily

/** The two font families of the app, both bundled (no requests to Google Fonts). */
@Immutable
class PixroostFonts(
    /** Unbounded 600: section titles and the big figures. */
    val display: FontFamily,
    /** Onest 400 and 600: everything else. */
    val ui: FontFamily,
)
