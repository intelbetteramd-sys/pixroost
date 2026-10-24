package app.pixroost.designsystem.theme

import androidx.compose.ui.graphics.Color
import app.pixroost.designsystem.LampColor

/** Values of the dark theme; the source is docs/design/mockups/foundation.html, `[data-theme="dark"]`. */
internal val PixroostDarkColors = PixroostColors(
    background = Color(0xFF0F1528),
    surface = Color(0xFF182038),
    text = Color(0xFFE8ECF4),
    textSecondary = Color(0xFFA3ADC2),
    line = Color(0xFF2A3350),
    primary = Color(0xFF9DB0F0),
    onPrimary = Color(0xFF0F1528),
    tonal = Color(0xFF26345E),
    onTonal = Color(0xFFE8ECF4),
    lamp = LampColor,
    onLamp = Color(0xFF172036),
    lampTint = Color(0xFF372C18),
    danger = Color(0xFFFF8A7A),
    onDanger = Color(0xFF0F1528),
    dangerTint = Color(0xFF3D1C22),
    inverse = Color(0xFFE8ECF4),
    onInverse = Color(0xFF172036),
    inverseAccent = Color(0xFF22305C),
    outline = Color(0xFFA3ADC2),
    focus = Color(0xFF9DB0F0),
    // 64% of #050812.
    scrim = Color(0xA3050812),
    // Black at 48% and 32%.
    shadowKey = Color(0x7A000000),
    shadowAmbient = Color(0x52000000),
    isDark = true,
)
