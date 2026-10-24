package app.pixroost.database

import app.pixroost.core.dispatchers.DefaultDispatcherProvider
import java.nio.file.Files

/** A real database file in a temporary folder, opened like in the app: bundled SQLite driver, WAL. */
class TemporaryDatabase : AutoCloseable {
    private val directory = Files.createTempDirectory("pixroost-db")

    val database: PixroostDatabase = pixroostDatabaseBuilder(directory.toFile())
        .applyPixroostDefaults(DefaultDispatcherProvider)
        .build()

    override fun close() {
        database.close()
        directory.toFile().deleteRecursively()
    }
}
