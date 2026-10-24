package app.pixroost.app.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NavigationStateTest {
    private val mobile = AppSections.forPlatform(AppPlatform.Mobile)
    private val desktop = AppSections.forPlatform(AppPlatform.Desktop)

    @Test
    fun startsOnFeedWithOnlyFeedInBackStack() {
        val state = NavigationState(mobile)

        assertEquals(AppDestination.Feed, state.current.value)
        assertEquals(listOf<AppDestination>(AppDestination.Feed), state.backStack)
        assertFalse(state.canGoBack)
    }

    @Test
    fun navigatingToAnotherSectionKeepsFeedUnderIt() {
        val state = NavigationState(mobile)

        assertTrue(state.navigateTo(AppDestination.Cleanup))

        assertEquals(AppDestination.Cleanup, state.current.value)
        assertEquals(listOf(AppDestination.Feed, AppDestination.Cleanup), state.backStack)
        assertTrue(state.canGoBack)
    }

    @Test
    fun switchingBetweenSectionsReplacesTheOldOne() {
        val state = NavigationState(mobile)

        state.navigateTo(AppDestination.Storages)
        state.navigateTo(AppDestination.Devices)

        assertEquals(listOf(AppDestination.Feed, AppDestination.Devices), state.backStack)
    }

    @Test
    fun settingsIsReachableOnEveryPlatform() {
        listOf(mobile, desktop).forEach { sections ->
            val state = NavigationState(sections)
            assertTrue(state.navigateTo(AppDestination.Settings))
            assertEquals(AppDestination.Settings, state.current.value)
        }
    }

    @Test
    fun backReturnsToFeedFromAnySection() {
        val state = NavigationState(mobile)
        state.navigateTo(AppDestination.Devices)

        assertTrue(state.back())

        assertEquals(AppDestination.Feed, state.current.value)
    }

    @Test
    fun backOnFeedIsNotHandledSoTheAppCanClose() {
        val state = NavigationState(mobile)

        assertFalse(state.back())
        assertEquals(AppDestination.Feed, state.current.value)
    }

    @Test
    fun archiveExistsOnlyOnDesktop() {
        val phone = NavigationState(mobile)
        val computer = NavigationState(desktop)

        assertFalse(phone.navigateTo(AppDestination.Archive))
        assertEquals(AppDestination.Feed, phone.current.value)
        assertTrue(computer.navigateTo(AppDestination.Archive))
    }

    @Test
    fun savedPositionIsRestored() {
        val state = NavigationState(desktop)
        state.navigateTo(AppDestination.Archive)

        val restored = NavigationState.restore(desktop, state.save())

        assertEquals(AppDestination.Archive, restored.current.value)
        assertEquals(state.backStack, restored.backStack)
    }

    @Test
    fun staleOrMissingSavedPositionOpensFeed() {
        assertEquals(AppDestination.Feed, NavigationState.restore(mobile, null).current.value)
        assertEquals(AppDestination.Feed, NavigationState.restore(mobile, "unknown").current.value)
        // A position saved on a computer that this platform does not have.
        assertEquals(AppDestination.Feed, NavigationState.restore(mobile, AppDestination.Archive.id).current.value)
    }

    @Test
    fun initialDestinationOutsideTheSectionsFallsBackToFeed() {
        val state = NavigationState(mobile, AppDestination.Archive)

        assertEquals(AppDestination.Feed, state.current.value)
    }
}
