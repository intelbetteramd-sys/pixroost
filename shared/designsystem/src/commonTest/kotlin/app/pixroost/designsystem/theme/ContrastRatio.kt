package app.pixroost.designsystem.theme

import androidx.compose.ui.graphics.Color
import kotlin.math.pow

/** WCAG 2.x contrast ratio of two opaque colors, 1.0 to 21.0. */
internal fun contrastRatio(first: Color, second: Color): Double = with(ContrastConstants) {
    val lighter = maxOf(luminance(first), luminance(second))
    val darker = minOf(luminance(first), luminance(second))
    (lighter + LUMINANCE_OFFSET) / (darker + LUMINANCE_OFFSET)
}

private fun luminance(color: Color): Double = with(ContrastConstants) {
    RED_WEIGHT * linear(color.red) + GREEN_WEIGHT * linear(color.green) + BLUE_WEIGHT * linear(color.blue)
}

private fun linear(channel: Float): Double = with(ContrastConstants) {
    if (channel <= LINEAR_THRESHOLD) {
        channel / LINEAR_DIVISOR
    } else {
        ((channel + GAMMA_OFFSET) / GAMMA_DIVISOR).toDouble().pow(GAMMA)
    }
}
