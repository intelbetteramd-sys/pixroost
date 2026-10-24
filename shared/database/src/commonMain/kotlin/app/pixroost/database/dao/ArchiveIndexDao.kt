package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import app.pixroost.database.entity.ArchiveIndexEntity

/** Which files are already in the archive of each computer. A cache: it is rebuilt by comparing with the computer. */
@Dao
interface ArchiveIndexDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entries: List<ArchiveIndexEntity>)

    @Query("SELECT EXISTS(SELECT 1 FROM archive_index WHERE device_id = :deviceId AND sha256 = :sha256)")
    suspend fun contains(deviceId: String, sha256: ByteArray): Boolean

    @Query("SELECT saved_path FROM archive_index WHERE device_id = :deviceId AND sha256 = :sha256")
    suspend fun getSavedPath(deviceId: String, sha256: ByteArray): String?

    @Query("DELETE FROM archive_index WHERE device_id = :deviceId")
    suspend fun clear(deviceId: String)
}
