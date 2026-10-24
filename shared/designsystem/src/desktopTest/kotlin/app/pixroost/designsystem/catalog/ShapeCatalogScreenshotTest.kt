package app.pixroost.designsystem.catalog

import app.pixroost.designsystem.theme.PixroostFormFactor
import kotlin.test.Test

class ShapeCatalogScreenshotTest {
    @Test
    fun `phone shapes in the light theme`() = captureCatalog(
        name = "shapes_phone_light",
        darkTheme = false,
        formFactor = PixroostFormFactor.Mobile,
    ) { ShapeCatalog() }

    @Test
    fun `phone shapes in the dark theme`() = captureCatalog(
        name = "shapes_phone_dark",
        darkTheme = true,
        formFactor = PixroostFormFactor.Mobile,
    ) { ShapeCatalog() }

    @Test
    fun `desktop shapes in the light theme`() = captureCatalog(
        name = "shapes_desktop_light",
        darkTheme = false,
        formFactor = PixroostFormFactor.Desktop,
    ) { ShapeCatalog() }
}
