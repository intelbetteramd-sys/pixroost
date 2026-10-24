package app.pixroost.database

import androidx.room3.Room
import androidx.room3.RoomDatabase
import java.io.File

/** Builder of the database in [directory] (the app data folder); finish it with [applyPixroostDefaults]. */
fun pixroostDatabaseBuilder(directory: File): RoomDatabase.Builder<PixroostDatabase> {
    directory.mkdirs()
    return Room.databaseBuilder<PixroostDatabase>(
        name = File(directory, DatabaseConstants.FILE_NAME).absolutePath,
        factory = { PixroostDatabaseConstructor.initialize() },
    )
}
