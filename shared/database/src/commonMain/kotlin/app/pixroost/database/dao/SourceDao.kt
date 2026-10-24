package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import app.pixroost.database.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

/** Connected sources. Deleting a source removes its copies (cascade) but never touches the files themselves. */
@Dao
interface SourceDao {
    @Insert
    suspend fun insert(source: SourceEntity): Long

    @Update
    suspend fun update(source: SourceEntity)

    @Delete
    suspend fun delete(source: SourceEntity)

    @Query("SELECT * FROM source WHERE id = :id")
    suspend fun getById(id: Long): SourceEntity?

    @Query("SELECT * FROM source ORDER BY id")
    suspend fun getAll(): List<SourceEntity>

    @Query("SELECT * FROM source ORDER BY id")
    fun observeAll(): Flow<List<SourceEntity>>
}
