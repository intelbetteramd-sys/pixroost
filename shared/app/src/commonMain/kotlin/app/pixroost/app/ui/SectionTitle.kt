package app.pixroost.app.ui

import androidx.compose.runtime.Composable
import app.pixroost.app.navigation.AppDestination
import app.pixroost.app.resources.Res
import app.pixroost.app.resources.nav_archive
import app.pixroost.app.resources.nav_cleanup
import app.pixroost.app.resources.nav_devices
import app.pixroost.app.resources.nav_feed
import app.pixroost.app.resources.nav_settings
import app.pixroost.app.resources.nav_storages
import org.jetbrains.compose.resources.stringResource

/** The localized name of [destination]. */
@Composable
internal fun sectionTitle(destination: AppDestination): String = stringResource(
    when (destination) {
        AppDestination.Feed -> Res.string.nav_feed
        AppDestination.Storages -> Res.string.nav_storages
        AppDestination.Cleanup -> Res.string.nav_cleanup
        AppDestination.Devices -> Res.string.nav_devices
        AppDestination.Archive -> Res.string.nav_archive
        AppDestination.Settings -> Res.string.nav_settings
    },
)
