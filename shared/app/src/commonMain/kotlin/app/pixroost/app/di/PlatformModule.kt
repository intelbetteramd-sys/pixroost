package app.pixroost.app.di

import app.pixroost.app.navigation.AppPlatform
import org.koin.core.module.Module
import org.koin.dsl.module

/** Koin module that tells the rest of the app which kind of device it runs on. */
fun platformModule(platform: AppPlatform): Module = module {
    single { platform }
}
