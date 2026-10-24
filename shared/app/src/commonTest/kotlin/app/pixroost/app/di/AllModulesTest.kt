package app.pixroost.app.di

import app.pixroost.app.navigation.AppPlatform
import app.pixroost.app.navigation.AppSections
import app.pixroost.core.dispatchers.DispatcherProvider
import org.koin.dsl.koinApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.time.Clock

class AllModulesTest {
    @Test
    fun `resolves core and app dependencies`() {
        val koin = koinApplication { modules(allModules(AppPlatform.Mobile)) }.koin

        assertSame(koin.get<DispatcherProvider>(), koin.get<DispatcherProvider>())
        koin.get<Clock>()
        assertEquals(AppPlatform.Mobile, koin.get<AppPlatform>())
    }

    @Test
    fun `sections follow the platform`() {
        val phone = koinApplication { modules(allModules(AppPlatform.Mobile)) }.koin.get<AppSections>()
        val computer = koinApplication { modules(allModules(AppPlatform.Desktop)) }.koin.get<AppSections>()

        assertEquals(4, phone.primary.size)
        assertEquals(5, computer.primary.size)
    }
}
