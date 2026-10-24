package app.pixroost.designsystem.catalog

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/** `#RRGGBB`, or `#RRGGBB / AA%` when the color is translucent. */
internal fun Color.toHexString(): String {
    val argb = toArgb()
    val rgb = (argb and CatalogConstants.RGB_MASK).toString(CatalogConstants.HEX_RADIX)
        .uppercase()
        .padStart(CatalogConstants.HEX_DIGITS, '0')
    val alphaPercent = (alpha * CatalogConstants.PERCENT).toInt()
    return if ((argb ushr CatalogConstants.ALPHA_SHIFT) == CatalogConstants.OPAQUE_ALPHA) {
        "#$rgb"
    } else {
        "#$rgb, $alphaPercent%"
    }
}
