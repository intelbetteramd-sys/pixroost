package app.pixroost.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppInfoTest {
    @Test
    fun `app name is Pixroost`() {
        assertEquals("Pixroost", AppInfo.NAME)
    }

    @Test
    fun `platform name is not blank`() {
        assertTrue(platformName().isNotBlank())
    }
}
