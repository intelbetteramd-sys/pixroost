package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import app.pixroost.database.entity.ConnectionLogEntity

/** Connection log kept by the computer. */
@Dao
interface ConnectionLogDao {
    @Insert
    suspend fun insert(entry: ConnectionLogEntity): Long

    @Query("SELECT * FROM connection_log WHERE device_id = :deviceId ORDER BY at DESC, id DESC LIMIT :limit")
    suspend fun getRecent(deviceId: String, limit: Int): List<ConnectionLogEntity>
}
