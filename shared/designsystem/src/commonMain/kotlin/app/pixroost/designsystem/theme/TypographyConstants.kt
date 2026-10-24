package app.pixroost.designsystem.theme

/** Sizes of the type scales in sp; docs/design/README.md, "Шрифты". Each size comes with its line height. */
internal object TypographyConstants {
    /** The big section title; the same on every form factor. */
    const val TITLE_SIZE = 28
    const val TITLE_LINE = 36

    /** Phone and tablet: 28/36, 18/24, 15/22, 13/18. */
    val MobileScale = TypeScale(
        figureSize = 28,
        figureLine = 36,
        headingSize = 18,
        headingLine = 24,
        bodySize = 15,
        bodyLine = 22,
        captionSize = 13,
        captionLine = 18,
    )

    /** Desktop, like the Fluent scale: 28/36, 20/28, 14/20, 12/16; the figure in a stat tile is 20. */
    val DesktopScale = TypeScale(
        figureSize = 20,
        figureLine = 28,
        headingSize = 20,
        headingLine = 28,
        bodySize = 14,
        bodyLine = 20,
        captionSize = 12,
        captionLine = 16,
    )

    /** Unbounded is wide; the mockups tighten it by -0.01 em. */
    const val DISPLAY_LETTER_SPACING_EM = -0.01f
}
