package app.pixroost.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.pixroost.app.di.startPixroostKoin
import app.pixroost.app.navigation.AppPlatform
import app.pixroost.app.ui.PixroostRoot
import app.pixroost.core.AppInfo
import app.pixroost.core.logging.AppLogging

fun main() {
    AppLogging.install(AppLogging.severityFrom(System.getProperty(DesktopConstants.LOG_LEVEL_PROPERTY)))
    startPixroostKoin(AppPlatform.Desktop)
    application {
        Window(onCloseRequest = ::exitApplication, title = AppInfo.NAME) {
            PixroostRoot()
        }
    }
}
