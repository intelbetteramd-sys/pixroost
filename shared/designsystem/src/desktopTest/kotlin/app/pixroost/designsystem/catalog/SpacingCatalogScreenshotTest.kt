package app.pixroost.designsystem.catalog

import app.pixroost.designsystem.theme.PixroostFormFactor
import kotlin.test.Test

class SpacingCatalogScreenshotTest {
    @Test
    fun `spacing scale in the light theme`() = captureCatalog(
        name = "spacing_light",
        darkTheme = false,
        formFactor = PixroostFormFactor.Mobile,
    ) { SpacingCatalog() }
}
