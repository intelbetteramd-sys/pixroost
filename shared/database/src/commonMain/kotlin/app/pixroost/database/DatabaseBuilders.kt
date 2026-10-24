package app.pixroost.database

import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.pixroost.core.dispatchers.DispatcherProvider

/**
 * Applies the settings every Pixroost database shares, whatever the platform:
 * the bundled SQLite (one version everywhere), WAL, migrations and the I/O dispatcher for queries.
 */
fun RoomDatabase.Builder<PixroostDatabase>.applyPixroostDefaults(
    dispatchers: DispatcherProvider,
): RoomDatabase.Builder<PixroostDatabase> = setDriver(BundledSQLiteDriver())
    .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
    .setQueryCoroutineContext(dispatchers.io)
    .addMigrations(*DatabaseMigrations.all.toTypedArray())
