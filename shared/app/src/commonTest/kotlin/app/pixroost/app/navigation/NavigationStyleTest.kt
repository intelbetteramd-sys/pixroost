package app.pixroost.app.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class NavigationStyleTest {
    @Test
    fun `phone width uses the bottom bar`() {
        assertEquals(NavigationStyle.BottomBar, NavigationStyle.forWindow(AppPlatform.Mobile, 411))
    }

    @Test
    fun `tablet width uses the rail`() {
        assertEquals(NavigationStyle.Rail, NavigationStyle.forWindow(AppPlatform.Mobile, 600))
        assertEquals(NavigationStyle.Rail, NavigationStyle.forWindow(AppPlatform.Mobile, 1280))
    }

    @Test
    fun `computer always uses the pane`() {
        assertEquals(NavigationStyle.Pane, NavigationStyle.forWindow(AppPlatform.Desktop, 500))
        assertEquals(NavigationStyle.Pane, NavigationStyle.forWindow(AppPlatform.Desktop, 1920))
    }
}
