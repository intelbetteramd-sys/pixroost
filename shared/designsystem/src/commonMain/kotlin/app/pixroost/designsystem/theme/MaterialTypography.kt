package app.pixroost.designsystem.theme

import androidx.compose.material3.Typography

/** Maps the text styles onto Material 3 roles; see [toMaterialColorScheme]. */
internal fun PixroostTypography.toMaterialTypography(): Typography = Typography(
    displayLarge = title,
    displayMedium = title,
    displaySmall = title,
    headlineLarge = title,
    headlineMedium = title,
    headlineSmall = heading,
    titleLarge = heading,
    titleMedium = bodyStrong,
    titleSmall = bodyStrong,
    bodyLarge = body,
    bodyMedium = body,
    bodySmall = caption,
    labelLarge = label,
    labelMedium = caption,
    labelSmall = caption,
)
