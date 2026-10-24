package app.pixroost.designsystem.theme

import androidx.compose.material3.Shapes

/** Maps the shapes onto Material 3 roles; see [toMaterialColorScheme]. */
internal fun PixroostShapes.toMaterialShapes(): Shapes = Shapes(
    extraSmall = field,
    small = small,
    medium = plate,
    large = card,
    extraLarge = sheet,
)
