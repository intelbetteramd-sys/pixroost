package app.pixroost.designsystem.theme

import androidx.compose.ui.graphics.Color
import kotlin.math.pow

/** WCAG 2.x contrast ratio of two opaque colors, 1.0 to 21.0. */
internal fun contrastRatio(first: Color, second: Color): Double {
    val lighter = maxOf(luminance(first), luminance(second))
    val darker = minOf(luminance(first), luminance(second))
    return (lighter + LUMINANCE_OFFSET) / (darker + LUMINANCE_OFFSET)
}

private fun luminance(color: Color): Double =
    RED_WEIGHT * linear(color.red) + GREEN_WEIGHT * linear(color.green) + BLUE_WEIGHT * linear(color.blue)

private fun linear(channel: Float): Double = if (channel <= LINEAR_THRESHOLD) {
    channel / LINEAR_DIVISOR
} else {
    ((channel + GAMMA_OFFSET) / GAMMA_DIVISOR).toDouble().pow(GAMMA)
}

private const val LUMINANCE_OFFSET = 0.05
private const val RED_WEIGHT = 0.2126
private const val GREEN_WEIGHT = 0.7152
private const val BLUE_WEIGHT = 0.0722
private const val LINEAR_THRESHOLD = 0.03928f
private const val LINEAR_DIVISOR = 12.92
private const val GAMMA_OFFSET = 0.055f
private const val GAMMA_DIVISOR = 1.055f
private const val GAMMA = 2.4
