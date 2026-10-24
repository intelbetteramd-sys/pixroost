package app.pixroost.designsystem.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runDesktopComposeUiTest
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import app.pixroost.designsystem.theme.PixroostFormFactor
import app.pixroost.designsystem.theme.PixroostTheme
import app.pixroost.designsystem.theme.PixroostTokens
import io.github.takahirom.roborazzi.captureRoboImage

/**
 * Draws [content] in the theme and compares it with (or records) the golden image `<name>.png`.
 * Without a Roborazzi task (`recordRoborazziDesktop`, `verifyRoborazziDesktop`) nothing is captured, so a plain
 * `check` only makes sure the catalog composes.
 */
@OptIn(ExperimentalTestApi::class)
internal fun captureCatalog(
    name: String,
    darkTheme: Boolean,
    formFactor: PixroostFormFactor,
    fontScale: Float = ScreenshotConstants.FONT_SCALE_NORMAL,
    content: @Composable () -> Unit,
) {
    val width = when (formFactor) {
        PixroostFormFactor.Mobile -> ScreenshotConstants.PHONE_WIDTH
        PixroostFormFactor.Desktop -> ScreenshotConstants.DESKTOP_WIDTH
    }
    runDesktopComposeUiTest(width = width, height = ScreenshotConstants.WINDOW_HEIGHT) {
        setContent {
            // The desktop has no system text scale, so the 200% case sets it by hand.
            CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, fontScale)) {
                PixroostTheme(darkTheme = darkTheme, formFactor = formFactor) {
                    Box(
                        Modifier
                            .testTag(ScreenshotConstants.ROOT_TAG)
                            .width(width.dp)
                            .background(PixroostTokens.colors.background),
                    ) {
                        content()
                    }
                }
            }
        }
        onNodeWithTag(ScreenshotConstants.ROOT_TAG).captureRoboImage("${ScreenshotConstants.GOLDEN_DIR}/$name.png")
    }
}
