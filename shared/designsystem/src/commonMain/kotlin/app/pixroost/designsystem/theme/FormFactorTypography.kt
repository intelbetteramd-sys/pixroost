package app.pixroost.designsystem.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// The mockups (CSS line-height) give every line its full height; Compose trims the first and the last line by default.
private val FullLineHeight = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.None)

/** The text styles of [formFactor] set in [fonts]. */
fun pixroostTypography(fonts: PixroostFonts, formFactor: PixroostFormFactor): PixroostTypography {
    val scale = when (formFactor) {
        PixroostFormFactor.Mobile -> TypographyConstants.MobileScale
        PixroostFormFactor.Desktop -> TypographyConstants.DesktopScale
    }

    // Hyphens.Auto needs a quality line breaker on Android; headings use the balanced one.
    fun display(family: FontFamily, size: Int, line: Int) = TextStyle(
        fontFamily = family,
        fontWeight = FontWeight.SemiBold,
        fontSize = size.sp,
        lineHeight = line.sp,
        lineHeightStyle = FullLineHeight,
        letterSpacing = TypographyConstants.DISPLAY_LETTER_SPACING_EM.em,
        lineBreak = LineBreak.Heading,
        hyphens = Hyphens.Auto,
    )

    fun text(weight: FontWeight, size: Int, line: Int, hyphens: Hyphens) = TextStyle(
        fontFamily = fonts.ui,
        fontWeight = weight,
        fontSize = size.sp,
        lineHeight = line.sp,
        lineHeightStyle = FullLineHeight,
        lineBreak = if (hyphens == Hyphens.Auto) LineBreak.Paragraph else LineBreak.Simple,
        hyphens = hyphens,
    )

    return PixroostTypography(
        title = display(fonts.display, TypographyConstants.TITLE_SIZE, TypographyConstants.TITLE_LINE),
        figure = display(fonts.display, scale.figureSize, scale.figureLine),
        heading = text(FontWeight.SemiBold, scale.headingSize, scale.headingLine, Hyphens.Auto),
        body = text(FontWeight.Normal, scale.bodySize, scale.bodyLine, Hyphens.Auto),
        bodyStrong = text(FontWeight.SemiBold, scale.bodySize, scale.bodyLine, Hyphens.Auto),
        label = text(FontWeight.SemiBold, scale.bodySize, scale.bodyLine, Hyphens.None),
        caption = text(FontWeight.Normal, scale.captionSize, scale.captionLine, Hyphens.None),
        navigationLabel = text(FontWeight.Normal, scale.captionSize, scale.captionLine, Hyphens.Auto),
    )
}
