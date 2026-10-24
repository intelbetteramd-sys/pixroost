package app.pixroost.app.di

import app.pixroost.app.navigation.AppPlatform
import app.pixroost.core.di.coreModule
import org.koin.core.module.Module

/**
 * All Koin modules of the app, one per layer. Each layer module (database, platform, domain, feature) is added
 * here when the layer appears.
 * Modules are built by functions: a module object keeps its singletons, so sharing one between containers
 * (as tests do) would leak state.
 */
fun allModules(platform: AppPlatform): List<Module> = listOf(
    coreModule(),
    platformModule(platform),
    shellModule(),
)
