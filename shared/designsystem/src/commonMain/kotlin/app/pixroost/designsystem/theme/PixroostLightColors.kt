package app.pixroost.designsystem.theme

import androidx.compose.ui.graphics.Color
import app.pixroost.designsystem.LampColor

/** Values of the light theme; the source is docs/design/mockups/foundation.html, `[data-theme="light"]`. */
internal val PixroostLightColors = PixroostColors(
    background = Color(0xFFF3F5F9),
    surface = Color(0xFFFFFFFF),
    text = Color(0xFF172036),
    textSecondary = Color(0xFF5B657A),
    line = Color(0xFFDDE2EB),
    primary = Color(0xFF22305C),
    onPrimary = Color(0xFFFFFFFF),
    tonal = Color(0xFFE4E8F3),
    onTonal = Color(0xFF22305C),
    lamp = LampColor,
    onLamp = Color(0xFF172036),
    lampTint = Color(0xFFFDF1DC),
    danger = Color(0xFFB42318),
    onDanger = Color(0xFFFFFFFF),
    dangerTint = Color(0xFFFBE9E7),
    inverse = Color(0xFF172036),
    onInverse = Color(0xFFFFFFFF),
    inverseAccent = Color(0xFF9DB0F0),
    outline = Color(0xFF5B657A),
    focus = Color(0xFF22305C),
    // 48% of #0F1528.
    scrim = Color(0x7A0F1528),
    // 16% and 8% of #172036.
    shadowKey = Color(0x29172036),
    shadowAmbient = Color(0x14172036),
    isDark = false,
)
