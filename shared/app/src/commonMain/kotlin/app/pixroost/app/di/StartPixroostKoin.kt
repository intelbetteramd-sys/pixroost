package app.pixroost.app.di

import app.pixroost.app.navigation.AppPlatform
import org.koin.core.context.startKoin

/** Starts the global Koin container with all app modules. Call once at app start, after logging is set up. */
fun startPixroostKoin(platform: AppPlatform) {
    startKoin { modules(allModules(platform)) }
}
