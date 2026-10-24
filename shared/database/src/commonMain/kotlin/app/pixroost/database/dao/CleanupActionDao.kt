package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import app.pixroost.database.entity.CleanupActionEntity
import kotlinx.coroutines.flow.Flow

/**
 * The cleanup journal. Entries are only added and moved through their statuses: there is no delete,
 * and no whole-row update that could rewrite the evidence (hashes, names, keeper) of a past action.
 * Each status change is its own guarded query.
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

    /**
     * PLANNED to DONE: the copy is in the trash. [trashExpiresAt] is null when the source does not say how long
     * it keeps it. Every transition returns the number of changed rows: 0 means the entry was not in the
     * expected status and nothing was written.
     */
    @Query(
        "UPDATE cleanup_action SET status = 'DONE', executed_at = :executedAt, trash_key = :trashKey, " +
            "trash_expires_at = :trashExpiresAt WHERE id = :id AND status = 'PLANNED'",
    )
    suspend fun markDone(id: Long, executedAt: Long, trashKey: String?, trashExpiresAt: Long?): Int

    /** PLANNED to REFUSED: the safety policy refused the action on the re-check. */
    @Query(
        "UPDATE cleanup_action SET status = 'REFUSED', denial_reason = :denialReason " +
            "WHERE id = :id AND status = 'PLANNED'",
    )
    suspend fun markRefused(id: Long, denialReason: String): Int

    /** PLANNED to FAILED: the source did not delete the copy. */
    @Query("UPDATE cleanup_action SET status = 'FAILED', error = :error WHERE id = :id AND status = 'PLANNED'")
    suspend fun markFailed(id: Long, error: String?): Int

    /** PLANNED to UNRECOVERABLE: Windows deleted the file permanently, so the deletion did happen at [executedAt]. */
    @Query(
        "UPDATE cleanup_action SET status = 'UNRECOVERABLE', executed_at = :executedAt, error = :error " +
            "WHERE id = :id AND status = 'PLANNED'",
    )
    suspend fun markUnrecoverable(id: Long, executedAt: Long, error: String?): Int

    /** DONE to RESTORED: the user brought the copy back from the trash. */
    @Query(
        "UPDATE cleanup_action SET status = 'RESTORED', restored_at = :restoredAt " +
            "WHERE id = :id AND status = 'DONE'",
    )
    suspend fun markRestored(id: Long, restoredAt: Long): Int

    /** Remembers that the user was warned about the lost keeper; the warning is shown once. */
    @Query(
        "UPDATE cleanup_action SET keeper_lost_notified_at = :notifiedAt " +
            "WHERE id = :id AND keeper_lost_notified_at IS NULL",
    )
    suspend fun markKeeperLostNotified(id: Long, notifiedAt: Long): Int
}
