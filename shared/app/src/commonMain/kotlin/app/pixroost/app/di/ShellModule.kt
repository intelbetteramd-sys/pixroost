package app.pixroost.app.di

import app.pixroost.app.navigation.AppSections
import org.koin.core.module.Module
import org.koin.dsl.module

/** Koin module of the app shell; needs an [app.pixroost.app.navigation.AppPlatform] from [platformModule]. */
fun shellModule(): Module = module {
    single { AppSections.forPlatform(get()) }
}
