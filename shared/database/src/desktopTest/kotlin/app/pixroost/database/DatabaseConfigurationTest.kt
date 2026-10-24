package app.pixroost.database

import androidx.room3.useWriterConnection
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DatabaseConfigurationTest {
    private val temporary = TemporaryDatabase()

    @AfterTest
    fun tearDown() = temporary.close()

    @Test
    fun usesWriteAheadLogging() = runTest {
        assertEquals("wal", pragma("journal_mode"))
    }

    @Test
    fun enforcesForeignKeys() = runTest {
        assertEquals("1", pragma("foreign_keys"))
    }

    private suspend fun pragma(name: String): String = temporary.database.useWriterConnection { connection ->
        connection.usePrepared("PRAGMA $name") { statement ->
            statement.step()
            statement.getText(0)
        }
    }
}
