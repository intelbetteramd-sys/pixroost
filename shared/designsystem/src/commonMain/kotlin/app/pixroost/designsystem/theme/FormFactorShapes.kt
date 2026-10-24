package app.pixroost.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/** The shapes of [formFactor]. */
fun pixroostShapes(formFactor: PixroostFormFactor): PixroostShapes {
    val smallest = RoundedCornerShape(size = ShapeConstants.RADIUS_SMALLEST.dp)
    val round = RoundedCornerShape(percent = ShapeConstants.FULL_PERCENT)
    return when (formFactor) {
        PixroostFormFactor.Mobile -> PixroostShapes(
            small = RoundedCornerShape(size = ShapeConstants.RADIUS_SMALL.dp),
            plate = RoundedCornerShape(size = ShapeConstants.RADIUS_PLATE.dp),
            card = RoundedCornerShape(size = ShapeConstants.RADIUS_CARD.dp),
            sheet = RoundedCornerShape(size = ShapeConstants.RADIUS_SHEET.dp),
            button = round,
            field = smallest,
            round = round,
        )
        PixroostFormFactor.Desktop -> PixroostShapes(
            small = smallest,
            plate = smallest,
            card = smallest,
            sheet = smallest,
            button = smallest,
            field = smallest,
            round = round,
        )
    }
}
