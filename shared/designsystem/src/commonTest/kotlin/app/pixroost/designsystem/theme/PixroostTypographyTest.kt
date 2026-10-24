package app.pixroost.designsystem.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import kotlin.test.Test
import kotlin.test.assertEquals

class PixroostTypographyTest {
    private val fonts = PixroostFonts(display = FontFamily.Serif, ui = FontFamily.SansSerif)
    private val mobile = pixroostTypography(fonts, PixroostFormFactor.Mobile)
    private val desktop = pixroostTypography(fonts, PixroostFormFactor.Desktop)

    @Test
    fun `the phone scale follows the design spec`() {
        assertEquals(28.sp to 36.sp, mobile.title.fontSize to mobile.title.lineHeight)
        assertEquals(18.sp to 24.sp, mobile.heading.fontSize to mobile.heading.lineHeight)
        assertEquals(15.sp to 22.sp, mobile.body.fontSize to mobile.body.lineHeight)
        assertEquals(13.sp to 18.sp, mobile.caption.fontSize to mobile.caption.lineHeight)
    }

    @Test
    fun `the desktop scale follows the design spec`() {
        assertEquals(28.sp to 36.sp, desktop.title.fontSize to desktop.title.lineHeight)
        assertEquals(20.sp to 28.sp, desktop.heading.fontSize to desktop.heading.lineHeight)
        assertEquals(14.sp to 20.sp, desktop.body.fontSize to desktop.body.lineHeight)
        assertEquals(12.sp to 16.sp, desktop.caption.fontSize to desktop.caption.lineHeight)
    }

    @Test
    fun `the figure is bigger on a phone than on the desktop`() {
        assertEquals(28.sp, mobile.figure.fontSize)
        assertEquals(20.sp, desktop.figure.fontSize)
    }

    @Test
    fun `body and list rows and headings and bottom bar labels hyphenate`() {
        for (typography in listOf(mobile, desktop)) {
            assertEquals(Hyphens.Auto, typography.body.hyphens)
            assertEquals(Hyphens.Auto, typography.bodyStrong.hyphens)
            assertEquals(Hyphens.Auto, typography.navigationLabel.hyphens)
            assertEquals(Hyphens.Auto, typography.heading.hyphens)
            assertEquals(Hyphens.Auto, typography.title.hyphens)
        }
    }

    @Test
    fun `button and rail labels do not hyphenate`() {
        for (typography in listOf(mobile, desktop)) {
            assertEquals(Hyphens.None, typography.label.hyphens)
            assertEquals(Hyphens.None, typography.caption.hyphens)
        }
    }

    @Test
    fun `every line keeps its full height`() {
        val full = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.None)
        val styles = with(mobile) { listOf(title, figure, heading, body, bodyStrong, label, caption, navigationLabel) }
        for (style in styles) {
            assertEquals(full, style.lineHeightStyle)
        }
    }

    @Test
    fun `titles use the display family and the rest the interface family`() {
        assertEquals(FontFamily.Serif, mobile.title.fontFamily)
        assertEquals(FontFamily.Serif, mobile.figure.fontFamily)
        assertEquals(FontFamily.SansSerif, mobile.body.fontFamily)
        assertEquals(FontFamily.SansSerif, mobile.label.fontFamily)
    }

    @Test
    fun `weights are semibold for emphasis and normal for text`() {
        assertEquals(FontWeight.SemiBold, mobile.title.fontWeight)
        assertEquals(FontWeight.SemiBold, mobile.heading.fontWeight)
        assertEquals(FontWeight.SemiBold, mobile.bodyStrong.fontWeight)
        assertEquals(FontWeight.Normal, mobile.body.fontWeight)
        assertEquals(FontWeight.Normal, mobile.caption.fontWeight)
    }
}
