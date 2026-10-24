package app.pixroost.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

/**
 * The theme of the app: color roles, fonts, shapes, and the Material 3 theme the components build on.
 * Follows the system light or dark setting unless [darkTheme] says otherwise.
 * Read the tokens through [PixroostTokens]; spacing is [PixroostSpacing].
 */
@Composable
fun PixroostTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    formFactor: PixroostFormFactor = defaultFormFactor(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) PixroostDarkColors else PixroostLightColors
    val fonts = rememberPixroostFonts()
    val typography = remember(fonts, formFactor) { pixroostTypography(fonts, formFactor) }
    val shapes = remember(formFactor) { pixroostShapes(formFactor) }
    CompositionLocalProvider(
        LocalPixroostColors provides colors,
        LocalPixroostTypography provides typography,
        LocalPixroostShapes provides shapes,
        LocalPixroostFormFactor provides formFactor,
    ) {
        MaterialTheme(
            colorScheme = remember(colors) { colors.toMaterialColorScheme() },
            typography = remember(typography) { typography.toMaterialTypography() },
            shapes = remember(shapes) { shapes.toMaterialShapes() },
            content = content,
        )
    }
}
