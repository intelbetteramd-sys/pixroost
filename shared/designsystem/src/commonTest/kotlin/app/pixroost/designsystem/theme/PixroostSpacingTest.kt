package app.pixroost.designsystem.theme

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class PixroostSpacingTest {
    @Test
    fun `the scale has the steps of the design spec`() {
        val steps = with(PixroostSpacing) { listOf(space4, space8, space12, space16, space24, space32, space48) }
        assertEquals(listOf(4.dp, 8.dp, 12.dp, 16.dp, 24.dp, 32.dp, 48.dp), steps)
    }
}
