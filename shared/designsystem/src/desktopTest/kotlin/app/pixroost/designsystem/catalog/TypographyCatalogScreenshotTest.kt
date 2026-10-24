package app.pixroost.designsystem.catalog

import app.pixroost.designsystem.theme.PixroostFormFactor
import kotlin.test.Test

class TypographyCatalogScreenshotTest {
    @Test
    fun `phone type scale in the light theme`() = captureCatalog(
        name = "typography_phone_light",
        darkTheme = false,
        formFactor = PixroostFormFactor.Mobile,
    ) { TypographyCatalog() }

    @Test
    fun `phone type scale in the dark theme`() = captureCatalog(
        name = "typography_phone_dark",
        darkTheme = true,
        formFactor = PixroostFormFactor.Mobile,
    ) { TypographyCatalog() }

    @Test
    fun `desktop type scale in the light theme`() = captureCatalog(
        name = "typography_desktop_light",
        darkTheme = false,
        formFactor = PixroostFormFactor.Desktop,
    ) { TypographyCatalog() }

    @Test
    fun `desktop type scale in the dark theme`() = captureCatalog(
        name = "typography_desktop_dark",
        darkTheme = true,
        formFactor = PixroostFormFactor.Desktop,
    ) { TypographyCatalog() }

    @Test
    fun `phone type scale at 200 percent text`() = captureCatalog(
        name = "typography_phone_light_200",
        darkTheme = false,
        formFactor = PixroostFormFactor.Mobile,
        fontScale = ScreenshotConstants.FONT_SCALE_LARGEST,
    ) { TypographyCatalog() }

    @Test
    fun `desktop type scale at 200 percent text`() = captureCatalog(
        name = "typography_desktop_light_200",
        darkTheme = false,
        formFactor = PixroostFormFactor.Desktop,
        fontScale = ScreenshotConstants.FONT_SCALE_LARGEST,
    ) { TypographyCatalog() }
}
