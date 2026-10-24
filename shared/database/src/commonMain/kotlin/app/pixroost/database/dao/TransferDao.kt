package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import app.pixroost.database.entity.TransferItemEntity
import app.pixroost.database.entity.TransferJobEntity
import app.pixroost.database.model.TransferItemStatus
import app.pixroost.database.model.TransferJobStatus
import kotlinx.coroutines.flow.Flow

/** The transfer queue: jobs and their files. */
@Dao
interface TransferDao {
    @Insert
    suspend fun insertJob(job: TransferJobEntity): Long

    @Insert
    suspend fun insertItems(items: List<TransferItemEntity>)

    @Query("SELECT * FROM transfer_job ORDER BY created_at DESC, id DESC")
    fun observeJobs(): Flow<List<TransferJobEntity>>

    @Query("SELECT * FROM transfer_item WHERE job_id = :jobId ORDER BY id")
    suspend fun getItems(jobId: Long): List<TransferItemEntity>

    @Query("UPDATE transfer_job SET status = :status WHERE id = :jobId")
    suspend fun setJobStatus(jobId: Long, status: TransferJobStatus)

    @Query("UPDATE transfer_item SET bytes_sent = :bytesSent, upload_id = :uploadId WHERE id = :itemId")
    suspend fun setItemProgress(itemId: Long, bytesSent: Long, uploadId: String?)

    @Query(
        "UPDATE transfer_item SET status = :status, error = :error, saved_path = :savedPath, " +
            "finished_at = :finishedAt WHERE id = :itemId",
    )
    suspend fun finishItem(
        itemId: Long,
        status: TransferItemStatus,
        error: String?,
        savedPath: String?,
        finishedAt: Long,
    )
}
