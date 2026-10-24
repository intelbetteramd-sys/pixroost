package app.pixroost.database

import androidx.room3.Room
import androidx.room3.RoomDatabase

/** Builder of the database in [directoryPath] (Application Support); finish it with [applyPixroostDefaults]. */
fun pixroostDatabaseBuilder(directoryPath: String): RoomDatabase.Builder<PixroostDatabase> =
    Room.databaseBuilder<PixroostDatabase>(
        name = "$directoryPath/${DatabaseConstants.FILE_NAME}",
        factory = { PixroostDatabaseConstructor.initialize() },
    )
