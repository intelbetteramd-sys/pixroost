package app.pixroost.designsystem.theme

import androidx.compose.ui.graphics.toArgb

/** Every role of [PixroostColors] by name as an ARGB value, for comparing with the spec table. */
internal fun PixroostColors.argbByRole(): Map<String, Long> = mapOf(
    "background" to background,
    "surface" to surface,
    "text" to text,
    "textSecondary" to textSecondary,
    "line" to line,
    "primary" to primary,
    "onPrimary" to onPrimary,
    "tonal" to tonal,
    "onTonal" to onTonal,
    "lamp" to lamp,
    "onLamp" to onLamp,
    "lampTint" to lampTint,
    "danger" to danger,
    "onDanger" to onDanger,
    "dangerTint" to dangerTint,
    "inverse" to inverse,
    "onInverse" to onInverse,
    "inverseAccent" to inverseAccent,
    "outline" to outline,
    "focus" to focus,
    "scrim" to scrim,
    "shadowKey" to shadowKey,
    "shadowAmbient" to shadowAmbient,
).mapValues { (_, color) -> color.toArgb().toUInt().toLong() }
