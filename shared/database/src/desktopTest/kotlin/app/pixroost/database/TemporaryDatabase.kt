package app.pixroost.database

import androidx.room3.useWriterConnection
import app.pixroost.core.dispatchers.DefaultDispatcherProvider
import java.nio.file.Files

/** A real database file in a temporary folder, opened like in the app: bundled SQLite driver, WAL. */
class TemporaryDatabase : AutoCloseable {
    private val directory = Files.createTempDirectory("pixroost-db")

    val database: PixroostDatabase = pixroostDatabaseBuilder(directory.toFile())
        .applyPixroostDefaults(DefaultDispatcherProvider)
        .build()

    /** Runs raw SQL, for rows the DAOs deliberately cannot delete (assets, groups, jobs). */
    suspend fun execute(sql: String) {
        database.useWriterConnection { connection -> connection.usePrepared(sql) { it.step() } }
    }

    override fun close() {
        database.close()
        directory.toFile().deleteRecursively()
    }
}
