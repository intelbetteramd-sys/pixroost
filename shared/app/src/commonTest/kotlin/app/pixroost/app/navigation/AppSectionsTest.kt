package app.pixroost.app.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AppSectionsTest {
    @Test
    fun phoneHasFourMainSections() {
        val sections = AppSections.forPlatform(AppPlatform.Mobile)

        assertEquals(
            listOf(AppDestination.Feed, AppDestination.Storages, AppDestination.Cleanup, AppDestination.Devices),
            sections.primary,
        )
        assertEquals(AppDestination.Feed, sections.start)
    }

    @Test
    fun computerHasFiveMainSectionsWithArchive() {
        val sections = AppSections.forPlatform(AppPlatform.Desktop)

        assertEquals(
            listOf(
                AppDestination.Feed,
                AppDestination.Storages,
                AppDestination.Cleanup,
                AppDestination.Archive,
                AppDestination.Devices,
            ),
            sections.primary,
        )
    }

    @Test
    fun idsAreUnique() {
        val ids = AppSections.forPlatform(AppPlatform.Desktop).all.map { it.id }

        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun findReturnsOnlyKnownSections() {
        val sections = AppSections.forPlatform(AppPlatform.Mobile)

        assertEquals(AppDestination.Settings, sections.find("settings"))
        assertNull(sections.find("archive"))
    }

    @Test
    fun emptyPrimaryListIsRejected() {
        assertFailsWith<IllegalArgumentException> { AppSections(emptyList(), emptyList()) }
    }
}
