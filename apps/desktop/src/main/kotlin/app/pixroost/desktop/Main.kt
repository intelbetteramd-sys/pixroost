package app.pixroost.desktop

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.pixroost.desktop.spike.ui.PhashSpikeController
import app.pixroost.desktop.spike.ui.PhashSpikeScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/** Spike S-06: the similar-photos window instead of the placeholder screen. */
fun main() = application {
    val controller = remember { PhashSpikeController(CoroutineScope(SupervisorJob() + Dispatchers.Default)) }
    Window(
        onCloseRequest = {
            controller.close()
            exitApplication()
        },
        title = "Pixroost S-06",
        state = rememberWindowState(width = 1200.dp, height = 850.dp),
    ) {
        MaterialTheme(colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
            PhashSpikeScreen(controller)
        }
    }
}
