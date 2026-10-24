package app.pixroost.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme

/**
 * Maps the roles onto Material 3, so the Material components that the design system builds on look right.
 * The builder is the same for both themes: every role the design uses is set, the values do not come from
 * Material's baseline. Dynamic color is not used (docs/design/README.md).
 */
internal fun PixroostColors.toMaterialColorScheme(): ColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = tonal,
    onPrimaryContainer = onTonal,
    inversePrimary = inverseAccent,
    secondary = primary,
    onSecondary = onPrimary,
    secondaryContainer = tonal,
    onSecondaryContainer = onTonal,
    tertiary = primary,
    onTertiary = onPrimary,
    tertiaryContainer = lampTint,
    onTertiaryContainer = text,
    background = background,
    onBackground = text,
    surface = surface,
    onSurface = text,
    surfaceVariant = tonal,
    onSurfaceVariant = textSecondary,
    surfaceTint = primary,
    inverseSurface = inverse,
    inverseOnSurface = onInverse,
    error = danger,
    onError = onDanger,
    errorContainer = dangerTint,
    onErrorContainer = text,
    outline = outline,
    outlineVariant = line,
    // Material applies its own alpha to the scrim, so the color goes in opaque.
    scrim = scrim.copy(alpha = 1f),
    surfaceBright = surface,
    surfaceDim = background,
    surfaceContainer = surface,
    surfaceContainerHigh = surface,
    surfaceContainerHighest = surface,
    surfaceContainerLow = surface,
    surfaceContainerLowest = surface,
)
