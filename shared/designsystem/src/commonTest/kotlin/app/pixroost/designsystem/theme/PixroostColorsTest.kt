package app.pixroost.designsystem.theme

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PixroostColorsTest {
    @Test
    fun `every role of the light theme has the value of the spec`() =
        assertEquals(LIGHT_SPEC, PixroostLightColors.argbByRole())

    @Test
    fun `every role of the dark theme has the value of the spec`() =
        assertEquals(DARK_SPEC, PixroostDarkColors.argbByRole())

    @Test
    fun `every text pair of the light theme passes WCAG AA`() = assertTextPairsPass(PixroostLightColors)

    @Test
    fun `every text pair of the dark theme passes WCAG AA`() = assertTextPairsPass(PixroostDarkColors)

    @Test
    fun `non text elements of the light theme have 3 to 1`() = assertNonTextPairsPass(PixroostLightColors)

    @Test
    fun `non text elements of the dark theme have 3 to 1`() = assertNonTextPairsPass(PixroostDarkColors)

    @Test
    fun `the lamp and its text do not change with the theme`() {
        assertEquals(PixroostLightColors.lamp, PixroostDarkColors.lamp)
        assertEquals(PixroostLightColors.onLamp, PixroostDarkColors.onLamp)
    }

    @Test
    fun `the themes know whether they are dark`() {
        assertTrue(PixroostDarkColors.isDark)
        assertTrue(!PixroostLightColors.isDark)
    }

    private fun assertTextPairsPass(colors: PixroostColors) {
        val pairs = mapOf(
            "text on background" to (colors.text to colors.background),
            "text on surface" to (colors.text to colors.surface),
            "text on tonal" to (colors.text to colors.tonal),
            "text on lampTint" to (colors.text to colors.lampTint),
            "text on dangerTint" to (colors.text to colors.dangerTint),
            "textSecondary on background" to (colors.textSecondary to colors.background),
            "textSecondary on surface" to (colors.textSecondary to colors.surface),
            "onPrimary on primary" to (colors.onPrimary to colors.primary),
            "onTonal on tonal" to (colors.onTonal to colors.tonal),
            "onLamp on lamp" to (colors.onLamp to colors.lamp),
            "onDanger on danger" to (colors.onDanger to colors.danger),
            "danger on dangerTint" to (colors.danger to colors.dangerTint),
            "danger on surface" to (colors.danger to colors.surface),
            "primary on background" to (colors.primary to colors.background),
            "primary on surface" to (colors.primary to colors.surface),
            "onInverse on inverse" to (colors.onInverse to colors.inverse),
            "inverseAccent on inverse" to (colors.inverseAccent to colors.inverse),
            "outline on surface" to (colors.outline to colors.surface),
        )
        assertContrast(pairs, MIN_TEXT_CONTRAST)
    }

    private fun assertNonTextPairsPass(colors: PixroostColors) {
        val pairs = mapOf(
            "focus on background" to (colors.focus to colors.background),
            "focus on surface" to (colors.focus to colors.surface),
            "focus on tonal" to (colors.focus to colors.tonal),
        )
        assertContrast(pairs, MIN_NON_TEXT_CONTRAST)
    }

    private fun assertContrast(pairs: Map<String, Pair<Color, Color>>, min: Double) {
        for ((name, pair) in pairs) {
            val ratio = contrastRatio(pair.first, pair.second)
            assertTrue(ratio >= min, "$name: contrast $ratio is below $min")
        }
    }

    private companion object {
        const val MIN_TEXT_CONTRAST = 4.5
        const val MIN_NON_TEXT_CONTRAST = 3.0

        // docs/design/mockups/foundation.html, [data-theme="light"]
        val LIGHT_SPEC = mapOf(
            "background" to 0xFFF3F5F9L,
            "surface" to 0xFFFFFFFFL,
            "text" to 0xFF172036L,
            "textSecondary" to 0xFF5B657AL,
            "line" to 0xFFDDE2EBL,
            "primary" to 0xFF22305CL,
            "onPrimary" to 0xFFFFFFFFL,
            "tonal" to 0xFFE4E8F3L,
            "onTonal" to 0xFF22305CL,
            "lamp" to 0xFFF2A93BL,
            "onLamp" to 0xFF172036L,
            "lampTint" to 0xFFFDF1DCL,
            "danger" to 0xFFB42318L,
            "onDanger" to 0xFFFFFFFFL,
            "dangerTint" to 0xFFFBE9E7L,
            "inverse" to 0xFF172036L,
            "onInverse" to 0xFFFFFFFFL,
            "inverseAccent" to 0xFF9DB0F0L,
            "outline" to 0xFF5B657AL,
            "focus" to 0xFF22305CL,
            "scrim" to 0x7A0F1528L,
            "shadowKey" to 0x29172036L,
            "shadowAmbient" to 0x14172036L,
        )

        // docs/design/mockups/foundation.html, [data-theme="dark"]
        val DARK_SPEC = mapOf(
            "background" to 0xFF0F1528L,
            "surface" to 0xFF182038L,
            "text" to 0xFFE8ECF4L,
            "textSecondary" to 0xFFA3ADC2L,
            "line" to 0xFF2A3350L,
            "primary" to 0xFF9DB0F0L,
            "onPrimary" to 0xFF0F1528L,
            "tonal" to 0xFF26345EL,
            "onTonal" to 0xFFE8ECF4L,
            "lamp" to 0xFFF2A93BL,
            "onLamp" to 0xFF172036L,
            "lampTint" to 0xFF372C18L,
            "danger" to 0xFFFF8A7AL,
            "onDanger" to 0xFF0F1528L,
            "dangerTint" to 0xFF3D1C22L,
            "inverse" to 0xFFE8ECF4L,
            "onInverse" to 0xFF172036L,
            "inverseAccent" to 0xFF22305CL,
            "outline" to 0xFFA3ADC2L,
            "focus" to 0xFF9DB0F0L,
            "scrim" to 0xA3050812L,
            "shadowKey" to 0x7A000000L,
            "shadowAmbient" to 0x52000000L,
        )
    }
}
