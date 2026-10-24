package app.pixroost.desktop

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.isTraySupported
import androidx.compose.ui.window.rememberWindowState
import app.pixroost.core.AppInfo
import app.pixroost.desktop.spike.AutostartRegistry
import app.pixroost.desktop.spike.CommandRunner
import app.pixroost.desktop.spike.ConnectivityServer
import app.pixroost.desktop.spike.InstallStatus
import app.pixroost.desktop.spike.InstallStatusReader
import app.pixroost.desktop.spike.SpikeConstants
import app.pixroost.desktop.spike.SpikeStatusScreen
import app.pixroost.desktop.spike.createTrayIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Spike S-07: a minimal app to test the Windows installers (tray, autostart, firewall rule).
fun main() = application {
    val server = remember { ConnectivityServer() }
    var listenError by remember { mutableStateOf<String?>(null) }
    var status by remember { mutableStateOf<InstallStatus?>(null) }
    var requests by remember { mutableIntStateOf(0) }
    var windowVisible by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    fun refresh() {
        scope.launch {
            status = withContext(Dispatchers.IO) { InstallStatusReader.read() }
            requests = server.requests
        }
    }

    fun setAutostart(enabled: Boolean) {
        val executable = status?.executable ?: return
        scope.launch {
            withContext(Dispatchers.IO) { AutostartRegistry.setEnabled(enabled, executable) }
            refresh()
        }
    }

    LaunchedEffect(Unit) {
        listenError = withContext(Dispatchers.IO) { server.start() }
        refresh()
    }

    if (isTraySupported) {
        val icon = remember { createTrayIcon() }
        Tray(icon = icon, tooltip = AppInfo.NAME, onAction = { windowVisible = true }) {
            Item("Открыть") { windowVisible = true }
            CheckboxItem("Автозапуск", status?.autostartEnabled == true) { setAutostart(it) }
            Item("Выйти") {
                server.stop()
                exitApplication()
            }
        }
    }

    Window(
        visible = windowVisible,
        onCloseRequest = { if (isTraySupported) windowVisible = false else exitApplication() },
        title = "${AppInfo.NAME} S-07",
        state = rememberWindowState(
            size = DpSize(SpikeConstants.WINDOW_WIDTH_DP.dp, SpikeConstants.WINDOW_HEIGHT_DP.dp),
        ),
    ) {
        SpikeStatusScreen(
            status = status,
            listenError = listenError,
            requests = requests,
            onRefresh = ::refresh,
            onAutostartChange = ::setAutostart,
            onOpenStartupSettings = {
                scope.launch(Dispatchers.IO) {
                    CommandRunner.run("cmd", "/c", "start", "", SpikeConstants.STARTUP_SETTINGS_URI)
                }
            },
        )
    }
}
