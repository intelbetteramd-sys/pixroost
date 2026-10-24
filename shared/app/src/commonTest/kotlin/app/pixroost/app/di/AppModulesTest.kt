package app.pixroost.app.di

import app.pixroost.app.navigation.AppPlatform
import app.pixroost.app.navigation.AppSections
import app.pixroost.core.dispatchers.DispatcherProvider
import org.koin.dsl.koinApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.time.Clock

class AppModulesTest {
    @Test
    fun resolvesCoreAndAppDependencies() {
        val koin = koinApplication { modules(appModules(AppPlatform.Mobile)) }.koin

        assertSame(koin.get<DispatcherProvider>(), koin.get<DispatcherProvider>())
        koin.get<Clock>()
        assertEquals(AppPlatform.Mobile, koin.get<AppPlatform>())
    }

    @Test
    fun sectionsFollowThePlatform() {
        val phone = koinApplication { modules(appModules(AppPlatform.Mobile)) }.koin.get<AppSections>()
        val computer = koinApplication { modules(appModules(AppPlatform.Desktop)) }.koin.get<AppSections>()

        assertEquals(4, phone.primary.size)
        assertEquals(5, computer.primary.size)
    }
}
