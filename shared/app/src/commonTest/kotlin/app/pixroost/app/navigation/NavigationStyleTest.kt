package app.pixroost.app.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class NavigationStyleTest {
    @Test
    fun phoneWidthUsesBottomBar() {
        assertEquals(NavigationStyle.BottomBar, navigationStyleFor(AppPlatform.Mobile, 411))
    }

    @Test
    fun tabletWidthUsesRail() {
        assertEquals(NavigationStyle.Rail, navigationStyleFor(AppPlatform.Mobile, 600))
        assertEquals(NavigationStyle.Rail, navigationStyleFor(AppPlatform.Mobile, 1280))
    }

    @Test
    fun computerAlwaysUsesPane() {
        assertEquals(NavigationStyle.Pane, navigationStyleFor(AppPlatform.Desktop, 500))
        assertEquals(NavigationStyle.Pane, navigationStyleFor(AppPlatform.Desktop, 1920))
    }
}
