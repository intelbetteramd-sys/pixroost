package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import app.pixroost.database.entity.DeviceEntity
import kotlinx.coroutines.flow.Flow

/** Paired devices. A revoked device keeps its row, and no query here clears the revocation. */
@Dao
interface DeviceDao {
    /** Pairing. Fails for an id that is already there: a revoked device is never silently paired again. */
    @Insert
    suspend fun insert(device: DeviceEntity)

    @Query("SELECT * FROM device WHERE id = :id")
    suspend fun getById(id: String): DeviceEntity?

    @Query("SELECT * FROM device ORDER BY paired_at")
    fun observeAll(): Flow<List<DeviceEntity>>

    @Query("UPDATE device SET name = :name WHERE id = :id")
    suspend fun rename(id: String, name: String)

    @Query("UPDATE device SET revoked_at = :revokedAt WHERE id = :id AND revoked_at IS NULL")
    suspend fun markRevoked(id: String, revokedAt: Long): Int

    @Query("UPDATE device SET last_seen_at = :seenAt, last_address = :address WHERE id = :id")
    suspend fun updateLastSeen(id: String, seenAt: Long, address: String?)
}
