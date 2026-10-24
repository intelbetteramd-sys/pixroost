package app.pixroost.designsystem.theme

/** The WCAG 2.x formulas of relative luminance. */
internal object ContrastConstants {
    const val LUMINANCE_OFFSET = 0.05
    const val RED_WEIGHT = 0.2126
    const val GREEN_WEIGHT = 0.7152
    const val BLUE_WEIGHT = 0.0722
    const val LINEAR_THRESHOLD = 0.03928f
    const val LINEAR_DIVISOR = 12.92
    const val GAMMA_OFFSET = 0.055f
    const val GAMMA_DIVISOR = 1.055f
    const val GAMMA = 2.4
}
