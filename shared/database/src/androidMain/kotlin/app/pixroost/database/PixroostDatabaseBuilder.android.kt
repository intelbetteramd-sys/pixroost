package app.pixroost.database

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase

/** Builder of the database in the app's private storage; finish it with [applyPixroostDefaults]. */
fun pixroostDatabaseBuilder(context: Context): RoomDatabase.Builder<PixroostDatabase> {
    val appContext = context.applicationContext
    return Room.databaseBuilder<PixroostDatabase>(
        context = appContext,
        name = appContext.getDatabasePath(DatabaseConstants.FILE_NAME).absolutePath,
        factory = { PixroostDatabaseConstructor.initialize() },
    )
}
