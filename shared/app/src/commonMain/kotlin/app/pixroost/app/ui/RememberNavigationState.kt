package app.pixroost.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import app.pixroost.app.navigation.AppSections
import app.pixroost.app.navigation.NavigationState

/** A [NavigationState] that survives configuration change and, on Android, process death. */
@Composable
internal fun rememberNavigationState(sections: AppSections): NavigationState {
    val saver = Saver<NavigationState, String>(
        save = { it.save() },
        restore = { NavigationState.restore(sections, it) },
    )
    return rememberSaveable(sections, saver = saver) { NavigationState(sections) }
}
