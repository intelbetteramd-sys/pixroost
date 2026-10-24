package app.pixroost.database

import androidx.room3.RoomDatabase
import androidx.room3.testing.MigrationTestHelper
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.pixroost.core.dispatchers.DefaultDispatcherProvider
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * How migrations are tested. Each schema version is exported to `schemas/` by the Room plugin.
 * A new version adds a test that creates the database at the old version, fills it with rows,
 * runs `runMigrationsAndValidate` with [DatabaseMigrations.all] and checks that the rows survived.
 */
class DatabaseMigrationTest {
    private val directory: Path = Files.createTempDirectory("pixroost-migration")

    @get:Rule
    val helper = MigrationTestHelper(
        schemaDirectoryPath = schemaDirectory,
        databasePath = directory.resolve(DatabaseConstants.FILE_NAME),
        driver = BundledSQLiteDriver(),
        databaseClass = PixroostDatabase::class,
    )

    @AfterTest
    fun tearDown() {
        directory.toFile().deleteRecursively()
    }

    @Test
    fun everyVersionHasAnExportedSchema() {
        for (version in 1..DatabaseConstants.VERSION) {
            val file = schemaDirectory.resolve(PixroostDatabase::class.qualifiedName.orEmpty()).resolve("$version.json")
            assertTrue(Files.exists(file), "Missing exported schema $file; commit it with the schema change")
        }
    }

    @Test
    fun migrationsFormAChainToTheCurrentVersion() {
        val container = RoomDatabase.MigrationContainer().apply { addMigrations(DatabaseMigrations.all) }

        if (DatabaseConstants.VERSION > 1) {
            assertNotNull(container.findMigrationPath(start = 1, end = DatabaseConstants.VERSION))
        } else {
            assertTrue(DatabaseMigrations.all.isEmpty(), "Version 1 has nothing to migrate from")
        }
    }

    @Test
    fun databaseCreatedFromTheFirstSchemaOpensWithTheCurrentCode() = runTest {
        helper.createDatabase(version = 1).use { connection ->
            connection.prepare("INSERT INTO source (kind, title, status) VALUES ('DROPBOX', 'Dropbox', 'OK')")
                .use { it.step() }
        }

        val migrated = helper.runMigrationsAndValidate(DatabaseConstants.VERSION, DatabaseMigrations.all)
        migrated.close()

        val database = pixroostDatabaseBuilder(directory.toFile())
            .applyPixroostDefaults(DefaultDispatcherProvider)
            .build()
        try {
            assertEquals(listOf("Dropbox"), database.sourceDao().getAll().map { it.title })
        } finally {
            database.close()
        }
    }

    private companion object {
        val schemaDirectory: Path = Path.of("schemas")
    }
}
