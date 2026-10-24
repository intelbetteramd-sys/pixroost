package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import app.pixroost.database.entity.AssetEntity
import kotlinx.coroutines.flow.Flow

/** Logical photos and videos: the feed and the pHash work queue. */
@Dao
interface AssetDao {
    @Insert
    suspend fun insert(asset: AssetEntity): Long

    @Update
    suspend fun update(asset: AssetEntity)

    @Query("SELECT * FROM asset WHERE id = :id")
    suspend fun getById(id: Long): AssetEntity?

    /** One page of the feed, newest first; assets without a date come last. */
    @Query("SELECT * FROM asset ORDER BY taken_at DESC, id DESC LIMIT :limit OFFSET :offset")
    suspend fun getFeedPage(limit: Int, offset: Int): List<AssetEntity>

    @Query("SELECT COUNT(*) FROM asset")
    fun observeCount(): Flow<Int>

    /** Assets the indexer has not hashed yet. */
    @Query("SELECT * FROM asset WHERE phash IS NULL ORDER BY id LIMIT :limit")
    suspend fun getWithoutPhash(limit: Int): List<AssetEntity>

    @Query("UPDATE asset SET phash = :phash, is_blank = :isBlank WHERE id = :id")
    suspend fun setPhash(id: Long, phash: Long, isBlank: Boolean)

    @Query("UPDATE asset SET best_instance_id = :instanceId WHERE id = :id")
    suspend fun setBestInstance(id: Long, instanceId: Long?)
}
