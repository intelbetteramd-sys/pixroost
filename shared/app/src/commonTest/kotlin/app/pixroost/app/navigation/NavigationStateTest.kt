package app.pixroost.app.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NavigationStateTest {
    private val mobile = AppSections.forPlatform(AppPlatform.Mobile)
    private val desktop = AppSections.forPlatform(AppPlatform.Desktop)

    @Test
    fun `starts on Feed with only Feed in the back stack`() {
        val state = NavigationState(mobile)

        assertEquals(AppDestination.Feed, state.current)
        assertEquals(listOf<AppDestination>(AppDestination.Feed), state.backStack.value)
        assertFalse(state.canGoBack)
    }

    @Test
    fun `phone keeps Feed under another section`() {
        val state = NavigationState(mobile)

        assertTrue(state.navigateTo(AppDestination.Cleanup))

        assertEquals(AppDestination.Cleanup, state.current)
        assertEquals(listOf(AppDestination.Feed, AppDestination.Cleanup), state.backStack.value)
        assertTrue(state.canGoBack)
    }

    @Test
    fun `phone replaces the old section when switching`() {
        val state = NavigationState(mobile)

        state.navigateTo(AppDestination.Storages)
        state.navigateTo(AppDestination.Devices)

        assertEquals(listOf(AppDestination.Feed, AppDestination.Devices), state.backStack.value)
    }

    @Test
    fun `phone back from a section returns to Feed`() {
        val state = NavigationState(mobile)
        state.navigateTo(AppDestination.Devices)

        assertTrue(state.back())

        assertEquals(AppDestination.Feed, state.current)
    }

    @Test
    fun `phone back on Feed is not handled so the app can close`() {
        val state = NavigationState(mobile)

        assertFalse(state.back())
        assertEquals(AppDestination.Feed, state.current)
    }

    @Test
    fun `phone settings open on top of the current section and back returns there`() {
        val state = NavigationState(mobile)
        state.navigateTo(AppDestination.Cleanup)

        state.navigateTo(AppDestination.Settings)

        assertEquals(
            listOf(AppDestination.Feed, AppDestination.Cleanup, AppDestination.Settings),
            state.backStack.value,
        )
        assertEquals(AppDestination.Cleanup, state.selectedPrimary)

        assertTrue(state.back())
        assertEquals(AppDestination.Cleanup, state.current)
        assertTrue(state.back())
        assertEquals(AppDestination.Feed, state.current)
        assertFalse(state.back())
    }

    @Test
    fun `phone settings opened from Feed go back to Feed`() {
        val state = NavigationState(mobile)

        state.navigateTo(AppDestination.Settings)

        assertEquals(listOf(AppDestination.Feed, AppDestination.Settings), state.backStack.value)
        assertTrue(state.back())
        assertEquals(AppDestination.Feed, state.current)
    }

    @Test
    fun `phone opening a section while in settings leaves the settings`() {
        val state = NavigationState(mobile)
        state.navigateTo(AppDestination.Cleanup)
        state.navigateTo(AppDestination.Settings)

        state.navigateTo(AppDestination.Devices)

        assertEquals(listOf(AppDestination.Feed, AppDestination.Devices), state.backStack.value)
    }

    @Test
    fun `computer keeps one section on the stack`() {
        val state = NavigationState(desktop)

        state.navigateTo(AppDestination.Archive)

        assertEquals(listOf<AppDestination>(AppDestination.Archive), state.backStack.value)
        assertEquals(AppDestination.Archive, state.selectedPrimary)
    }

    @Test
    fun `computer back does nothing so Esc does not switch sections`() {
        val state = NavigationState(desktop)
        state.navigateTo(AppDestination.Archive)

        assertFalse(state.canGoBack)
        assertFalse(state.back())
        assertEquals(AppDestination.Archive, state.current)

        state.navigateTo(AppDestination.Settings)
        assertFalse(state.back())
        assertEquals(AppDestination.Settings, state.current)
    }

    @Test
    fun `Archive exists only on a computer`() {
        val phone = NavigationState(mobile)
        val computer = NavigationState(desktop)

        assertFalse(phone.navigateTo(AppDestination.Archive))
        assertEquals(AppDestination.Feed, phone.current)
        assertTrue(computer.navigateTo(AppDestination.Archive))
    }

    @Test
    fun `saved position is restored`() {
        val state = NavigationState(mobile)
        state.navigateTo(AppDestination.Cleanup)
        state.navigateTo(AppDestination.Settings)

        val restored = NavigationState.restore(mobile, state.save())

        assertEquals(state.backStack.value, restored.backStack.value)
    }

    @Test
    fun `saved computer position is restored`() {
        val state = NavigationState(desktop)
        state.navigateTo(AppDestination.Archive)

        val restored = NavigationState.restore(desktop, state.save())

        assertEquals(AppDestination.Archive, restored.current)
    }

    @Test
    fun `stale or missing saved position opens Feed`() {
        assertEquals(AppDestination.Feed, NavigationState.restore(mobile, null).current)
        assertEquals(AppDestination.Feed, NavigationState.restore(mobile, "unknown").current)
        // A position saved on a computer, with a section that a phone does not have.
        assertEquals(AppDestination.Feed, NavigationState.restore(mobile, AppDestination.Archive.id).current)
    }

    @Test
    fun `initial destination outside the sections falls back to Feed`() {
        val state = NavigationState(mobile, AppDestination.Archive)

        assertEquals(AppDestination.Feed, state.current)
    }
}
