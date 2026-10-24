package app.pixroost.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Color roles of the app. Screens use roles, never raw colors, so the light and the dark theme differ
 * only in the values behind them (docs/design/README.md, "Цвета" and "Тёмная тема").
 *
 * Colors the mockups call "fog", "paper", "ink", "slate", "dusk" and so on map to [background], [surface], [text],
 * [textSecondary] and [primary] in the light theme.
 */
@Immutable
class PixroostColors(
    /** Screen background ("fog"). */
    val background: Color,
    /** Cards, sheets and panels ("paper"). */
    val surface: Color,
    /** Primary text and icons ("ink"). */
    val text: Color,
    /** Secondary text ("slate"). */
    val textSecondary: Color,
    /** Decorative dividers and card borders ("line"). */
    val line: Color,
    /** The main action, selection, progress ("dusk"). */
    val primary: Color,
    val onPrimary: Color,
    /** Tonal buttons and the selected navigation item ("dusk-tint"). */
    val tonal: Color,
    val onTonal: Color,
    /** "Saved at home" and the cleanup button only. Does not change with the theme. */
    val lamp: Color,
    /** Text and the mark outline on [lamp]. Does not change with the theme. */
    val onLamp: Color,
    /** Background of the "saved" status and the safety rule ("lamp-tint"). */
    val lampTint: Color,
    /** Deletion only; the one exception is an input error. */
    val danger: Color,
    val onDanger: Color,
    val dangerTint: Color,
    /** The notification at the bottom of the screen (inverted surface). */
    val inverse: Color,
    val onInverse: Color,
    val inverseAccent: Color,
    /** Outlines of fields and of a switched-off switch. */
    val outline: Color,
    /** The focus ring. */
    val focus: Color,
    /** The dimming under sheets and dialogs; the alpha is part of the color. */
    val scrim: Color,
    /** The main (key) shadow of sheets, dialogs, snackbars and floating panels. */
    val shadowKey: Color,
    /** The soft second shadow that goes with [shadowKey]. */
    val shadowAmbient: Color,
    val isDark: Boolean,
)
