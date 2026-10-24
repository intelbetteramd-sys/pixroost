package app.pixroost.core.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AppLoggingTest {
    private class RecordingWriter : LogWriter() {
        val messages = mutableListOf<String>()

        override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
            messages += "$severity/$tag/$message"
        }
    }

    @AfterTest
    fun restoreDefaults() {
        AppLogging.install(Severity.Verbose)
    }

    @Test
    fun `drops messages below min severity`() {
        val writer = RecordingWriter()
        AppLogging.install(Severity.Info, listOf(writer))

        val logger = AppLogging.logger("Test")
        logger.d { "hidden" }
        logger.i { "shown" }

        assertEquals(listOf("Info/Test/shown"), writer.messages)
    }

    @Test
    fun `severity from setting defaults to info`() {
        assertEquals(Severity.Debug, AppLogging.severityFrom("DEBUG"))
        assertEquals(Severity.Info, AppLogging.severityFrom("info"))
        assertEquals(Severity.Info, AppLogging.severityFrom("verbose"))
        assertEquals(Severity.Info, AppLogging.severityFrom(null))
    }
}
