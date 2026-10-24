package app.pixroost.designsystem.theme

import androidx.compose.material3.Shapes

/**
 * Maps the shapes onto Material 3 roles; see [toMaterialColorScheme]. Material's cards take `medium`, so it is
 * the card shape (20 dp); plates and badges (16 dp) go to `large`, which nothing the design uses reads.
 * Material buttons take a full corner from their own tokens, not from here: on the desktop the buttons of
 * step 3.2 pass `PixroostTokens.shapes.button` (4 dp) explicitly.
 */
internal fun PixroostShapes.toMaterialShapes(): Shapes = Shapes(
    extraSmall = field,
    small = small,
    medium = card,
    large = plate,
    extraLarge = sheet,
)
