package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import app.pixroost.database.entity.SimilarGroupEntity
import app.pixroost.database.entity.SimilarMemberEntity
import kotlinx.coroutines.flow.Flow

/** Groups of similar shots and their members. */
@Dao
interface SimilarGroupDao {
    @Insert
    suspend fun insertGroup(group: SimilarGroupEntity): Long

    @Insert
    suspend fun insertMembers(members: List<SimilarMemberEntity>)

    @Query("SELECT * FROM similar_group WHERE dismissed = 0 ORDER BY created_at DESC, id DESC")
    fun observeActiveGroups(): Flow<List<SimilarGroupEntity>>

    @Query("SELECT * FROM similar_member WHERE group_id = :groupId ORDER BY asset_id")
    suspend fun getMembers(groupId: Long): List<SimilarMemberEntity>

    @Query("UPDATE similar_group SET dismissed = 1 WHERE id = :groupId")
    suspend fun dismiss(groupId: Long)
}
