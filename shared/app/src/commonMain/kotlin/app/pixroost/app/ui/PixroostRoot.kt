package app.pixroost.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.pixroost.app.navigation.AppPlatform
import app.pixroost.app.navigation.AppSections
import org.koin.compose.koinInject

/**
 * The app as the entry points start it: dependencies come from the Koin container
 * ([app.pixroost.app.di.startPixroostKoin]). Everything below takes them as parameters.
 */
@Composable
fun PixroostRoot(modifier: Modifier = Modifier) {
    PixroostApp(sections = koinInject<AppSections>(), platform = koinInject<AppPlatform>(), modifier = modifier)
}
