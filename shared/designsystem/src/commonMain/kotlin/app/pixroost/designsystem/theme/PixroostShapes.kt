package app.pixroost.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable

/** Corner shapes by role. On the desktop every role is 4 dp except [round], which stays round by its form. */
@Immutable
class PixroostShapes(
    /** Thumbnails, filter chips. */
    val small: RoundedCornerShape,
    /** Plates (`SafetyNote`, `InfoNote`) and badges. */
    val plate: RoundedCornerShape,
    /** Cards and lists. */
    val card: RoundedCornerShape,
    /** Bottom sheets and dialogs. */
    val sheet: RoundedCornerShape,
    /** Buttons: fully rounded on a phone. */
    val button: RoundedCornerShape,
    /** Input fields. */
    val field: RoundedCornerShape,
    /** Elements round by form: place marks, radio buttons, the switch track. */
    val round: RoundedCornerShape,
)
