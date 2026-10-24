package app.pixroost.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import app.pixroost.app.navigation.AppDestination
import app.pixroost.app.navigation.AppPlatform
import app.pixroost.app.navigation.AppSections
import kotlin.test.Test

// Starts the real Navigation 3 display: catches binary incompatibility with the Compose version in use.
@OptIn(ExperimentalTestApi::class)
class PixroostAppTest {
    @Test
    fun `computer pane switches sections`() = runComposeUiTest {
        setContent { PixroostApp(AppSections.forPlatform(AppPlatform.Desktop), AppPlatform.Desktop) }

        onNodeWithTag(NavigationTestTags.content(AppDestination.Feed)).assertExists()

        onNodeWithTag(NavigationTestTags.item(AppDestination.Archive)).performClick()
        onNodeWithTag(NavigationTestTags.content(AppDestination.Archive)).assertExists()

        onNodeWithTag(NavigationTestTags.item(AppDestination.Settings)).performClick()
        onNodeWithTag(NavigationTestTags.content(AppDestination.Settings)).assertExists()
    }

    @Test
    fun `phone bottom bar switches sections and opens settings`() = runComposeUiTest {
        setContent {
            Box(Modifier.width(PHONE_WIDTH)) {
                PixroostApp(AppSections.forPlatform(AppPlatform.Mobile), AppPlatform.Mobile)
            }
        }

        onNodeWithTag(NavigationTestTags.content(AppDestination.Feed)).assertExists()

        onNodeWithTag(NavigationTestTags.item(AppDestination.Cleanup)).performClick()
        onNodeWithTag(NavigationTestTags.content(AppDestination.Cleanup)).assertExists()

        onNodeWithTag(NavigationTestTags.item(AppDestination.Settings)).performClick()
        onNodeWithTag(NavigationTestTags.content(AppDestination.Settings)).assertExists()
    }

    @Test
    fun `tablet rail switches sections`() = runComposeUiTest {
        setContent { PixroostApp(AppSections.forPlatform(AppPlatform.Mobile), AppPlatform.Mobile) }

        onNodeWithTag(NavigationTestTags.item(AppDestination.Devices)).performClick()
        onNodeWithTag(NavigationTestTags.content(AppDestination.Devices)).assertExists()
    }

    private companion object {
        val PHONE_WIDTH = 400.dp
    }
}
