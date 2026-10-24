package app.pixroost.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

/** Access to the tokens of the closest [PixroostTheme]: `PixroostTokens.colors.primary`. */
object PixroostTokens {
    val colors: PixroostColors
        @Composable
        @ReadOnlyComposable
        get() = LocalPixroostColors.current

    val typography: PixroostTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalPixroostTypography.current

    val shapes: PixroostShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalPixroostShapes.current

    val formFactor: PixroostFormFactor
        @Composable
        @ReadOnlyComposable
        get() = LocalPixroostFormFactor.current
}
