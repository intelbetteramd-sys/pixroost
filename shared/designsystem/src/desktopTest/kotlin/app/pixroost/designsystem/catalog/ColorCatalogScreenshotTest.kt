package app.pixroost.designsystem.catalog

import app.pixroost.designsystem.theme.PixroostFormFactor
import kotlin.test.Test

class ColorCatalogScreenshotTest {
    @Test
    fun `colors in the light theme`() = captureCatalog(
        name = "colors_light",
        darkTheme = false,
        formFactor = PixroostFormFactor.Mobile,
    ) { ColorCatalog() }

    @Test
    fun `colors in the dark theme`() = captureCatalog(
        name = "colors_dark",
        darkTheme = true,
        formFactor = PixroostFormFactor.Mobile,
    ) { ColorCatalog() }
}
