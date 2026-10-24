package app.pixroost.core.di

import app.pixroost.core.dispatchers.DefaultDispatcherProvider
import app.pixroost.core.dispatchers.DispatcherProvider
import org.koin.dsl.module
import kotlin.time.Clock

/** Koin module of the core layer: dispatchers and the clock. */
val coreModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider }
    single<Clock> { Clock.System }
}
