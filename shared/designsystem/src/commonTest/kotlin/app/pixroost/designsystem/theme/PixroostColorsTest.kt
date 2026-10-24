package app.pixroost.designsystem.theme

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PixroostColorsTest {
    @Test
    fun `every text pair of the light theme passes WCAG AA`() = assertTextPairsPass(PixroostLightColors)

    @Test
    fun `every text pair of the dark theme passes WCAG AA`() = assertTextPairsPass(PixroostDarkColors)

    @Test
    fun `the lamp and its text do not change with the theme`() {
        assertEquals(PixroostLightColors.lamp, PixroostDarkColors.lamp)
        assertEquals(PixroostLightColors.onLamp, PixroostDarkColors.onLamp)
    }

    @Test
    fun `light values of key roles match the design spec`() {
        assertEquals(Color(0xFFF3F5F9), PixroostLightColors.background)
        assertEquals(Color(0xFF22305C), PixroostLightColors.primary)
        assertEquals(Color(0xFFF2A93B), PixroostLightColors.lamp)
        assertEquals(Color(0xFFB42318), PixroostLightColors.danger)
    }

    @Test
    fun `dark values of key roles match the design spec`() {
        assertEquals(Color(0xFF0F1528), PixroostDarkColors.background)
        assertEquals(Color(0xFF9DB0F0), PixroostDarkColors.primary)
        assertEquals(Color(0xFFFF8A7A), PixroostDarkColors.danger)
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
            "textSecondary on background" to (colors.textSecondary to colors.background),
            "textSecondary on surface" to (colors.textSecondary to colors.surface),
            "onPrimary on primary" to (colors.onPrimary to colors.primary),
            "onTonal on tonal" to (colors.onTonal to colors.tonal),
            "onLamp on lamp" to (colors.onLamp to colors.lamp),
            "text on lampTint" to (colors.text to colors.lampTint),
            "onDanger on danger" to (colors.onDanger to colors.danger),
            "danger on dangerTint" to (colors.danger to colors.dangerTint),
            "onInverse on inverse" to (colors.onInverse to colors.inverse),
            "inverseAccent on inverse" to (colors.inverseAccent to colors.inverse),
            "outline on surface" to (colors.outline to colors.surface),
        )
        for ((name, pair) in pairs) {
            val ratio = contrastRatio(pair.first, pair.second)
            assertTrue(ratio >= MIN_TEXT_CONTRAST, "$name: contrast $ratio is below $MIN_TEXT_CONTRAST")
        }
    }

    private companion object {
        const val MIN_TEXT_CONTRAST = 4.5
    }
}
