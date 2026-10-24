package app.pixroost.app.di

import app.pixroost.app.navigation.AppSections
import org.koin.dsl.module

/** Koin module of the app shell; needs an [app.pixroost.app.navigation.AppPlatform] from [platformModule]. */
val appModule = module {
    single { AppSections.forPlatform(get()) }
}
