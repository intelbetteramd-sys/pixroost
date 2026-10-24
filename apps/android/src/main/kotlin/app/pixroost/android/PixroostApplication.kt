package app.pixroost.android

import android.app.Application
import android.content.pm.ApplicationInfo
import app.pixroost.app.di.startPixroostKoin
import app.pixroost.app.navigation.AppPlatform
import app.pixroost.core.logging.AppLogging
import co.touchlab.kermit.Severity

class PixroostApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // The debuggable flag is set by the build type, so no BuildConfig is needed to tell debug from release.
        val debuggable = applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        AppLogging.install(if (debuggable) Severity.Debug else Severity.Info)
        startPixroostKoin(AppPlatform.Mobile)
    }
}
