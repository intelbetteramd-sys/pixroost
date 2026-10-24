package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import app.pixroost.database.entity.DeviceEntity
import kotlinx.coroutines.flow.Flow

/** Paired devices. A revoked device keeps its row: the revocation must keep rejecting its key. */
@Dao
interface DeviceDao {
    @Upsert
    suspend fun upsert(device: DeviceEntity)

    @Query("SELECT * FROM device WHERE id = :id")
    suspend fun getById(id: String): DeviceEntity?

    @Query("SELECT * FROM device ORDER BY paired_at")
    fun observeAll(): Flow<List<DeviceEntity>>

    @Query("UPDATE device SET revoked_at = :revokedAt WHERE id = :id")
    suspend fun markRevoked(id: String, revokedAt: Long)

    @Query("UPDATE device SET last_seen_at = :seenAt, last_address = :address WHERE id = :id")
    suspend fun updateLastSeen(id: String, seenAt: Long, address: String?)
}
