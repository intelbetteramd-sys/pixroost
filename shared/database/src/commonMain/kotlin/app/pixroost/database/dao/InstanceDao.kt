package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import app.pixroost.database.entity.InstanceEntity
import app.pixroost.database.model.InstanceState
import kotlinx.coroutines.flow.Flow

/** Physical copies of assets in sources. */
@Dao
interface InstanceDao {
    @Insert
    suspend fun insert(instance: InstanceEntity): Long

    @Update
    suspend fun update(instance: InstanceEntity)

    @Query("SELECT * FROM instance WHERE id = :id")
    suspend fun getById(id: Long): InstanceEntity?

    /** The sync looks a copy up by the key the source gave it. */
    @Query("SELECT * FROM instance WHERE source_id = :sourceId AND remote_key = :remoteKey")
    suspend fun findBySourceAndKey(sourceId: Long, remoteKey: String): InstanceEntity?

    @Query("SELECT * FROM instance WHERE asset_id = :assetId ORDER BY id")
    suspend fun getByAsset(assetId: Long): List<InstanceEntity>

    /** Feeds the "where it lies" card. */
    @Query("SELECT * FROM instance WHERE asset_id = :assetId ORDER BY id")
    fun observeByAsset(assetId: Long): Flow<List<InstanceEntity>>

    @Query("SELECT * FROM instance WHERE sha256 = :sha256")
    suspend fun findBySha256(sha256: ByteArray): List<InstanceEntity>

    @Query("SELECT * FROM instance WHERE md5 = :md5")
    suspend fun findByMd5(md5: ByteArray): List<InstanceEntity>

    @Query("UPDATE instance SET state = :state, last_seen_at = :seenAt WHERE id = :id")
    suspend fun setState(id: Long, state: InstanceState, seenAt: Long)

    @Query("SELECT COUNT(*) FROM instance WHERE source_id = :sourceId")
    suspend fun countBySource(sourceId: Long): Int
}
