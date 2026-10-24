package app.pixroost.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf

/** The form factor of the closest [PixroostTheme]. Read it through [PixroostTokens]. */
internal val LocalPixroostFormFactor = staticCompositionLocalOf<PixroostFormFactor> { noTheme() }
