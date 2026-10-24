package app.pixroost.database

import androidx.room3.RoomDatabaseConstructor

/** Room generates the `actual` object for every target; it builds [PixroostDatabase] without reflection. */
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object PixroostDatabaseConstructor : RoomDatabaseConstructor<PixroostDatabase> {
    override fun initialize(): PixroostDatabase
}
