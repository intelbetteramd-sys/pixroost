package app.pixroost.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class PixroostShapesTest {
    @Test
    fun `phone radii follow the design spec`() {
        val shapes = pixroostShapes(PixroostFormFactor.Mobile)
        assertEquals(RoundedCornerShape(8.dp), shapes.small)
        assertEquals(RoundedCornerShape(16.dp), shapes.plate)
        assertEquals(RoundedCornerShape(20.dp), shapes.card)
        assertEquals(RoundedCornerShape(28.dp), shapes.sheet)
        assertEquals(RoundedCornerShape(50), shapes.button)
        assertEquals(RoundedCornerShape(4.dp), shapes.field)
    }

    @Test
    fun `on the desktop everything the app draws is 4 dp dialogs included`() {
        val shapes = pixroostShapes(PixroostFormFactor.Desktop)
        val drawnByApp = listOf(shapes.small, shapes.plate, shapes.card, shapes.sheet, shapes.button, shapes.field)
        for (shape in drawnByApp) {
            assertEquals(RoundedCornerShape(4.dp), shape)
        }
    }

    @Test
    fun `round elements stay round on the desktop`() {
        assertEquals(RoundedCornerShape(50), pixroostShapes(PixroostFormFactor.Desktop).round)
    }
}
