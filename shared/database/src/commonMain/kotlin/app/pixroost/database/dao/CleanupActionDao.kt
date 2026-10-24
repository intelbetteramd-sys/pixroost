package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import app.pixroost.database.entity.CleanupActionEntity
import app.pixroost.database.model.CleanupStatus
import kotlinx.coroutines.flow.Flow

/**
 * The cleanup journal. Entries are only added and moved through their statuses: there is no delete,
 * and no whole-row update that could rewrite the evidence (hashes, names, keeper) of a past action.
 */
@Dao
interface CleanupActionDao {
    @Insert
    suspend fun insert(action: CleanupActionEntity): Long

    @Query("SELECT * FROM cleanup_action WHERE id = :id")
    suspend fun getById(id: Long): CleanupActionEntity?

    @Query("SELECT * FROM cleanup_action WHERE plan_id = :planId ORDER BY id")
    suspend fun getByPlan(planId: String): List<CleanupActionEntity>

    @Query("SELECT * FROM cleanup_action ORDER BY id DESC")
    fun observeAll(): Flow<List<CleanupActionEntity>>

    /** The copy is in the trash. [trashExpiresAt] is null when the source does not say how long it keeps it. */
    @Query(
        "UPDATE cleanup_action SET status = 'DONE', executed_at = :executedAt, trash_key = :trashKey, " +
            "trash_expires_at = :trashExpiresAt WHERE id = :id",
    )
    suspend fun markDone(id: Long, executedAt: Long, trashKey: String?, trashExpiresAt: Long?)

    /** The action ended as [CleanupStatus.FAILED], [CleanupStatus.REFUSED] or [CleanupStatus.UNRECOVERABLE]. */
    @Query("UPDATE cleanup_action SET status = :status, denial_reason = :denialReason, error = :error WHERE id = :id")
    suspend fun markNotDone(id: Long, status: CleanupStatus, denialReason: String?, error: String?)

    @Query("UPDATE cleanup_action SET status = :status, restored_at = :restoredAt WHERE id = :id")
    suspend fun setRestored(id: Long, status: CleanupStatus, restoredAt: Long)
}
