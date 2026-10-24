package app.pixroost.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import app.pixroost.database.model.TransferItemStatus

/** One file of a [TransferJobEntity]. */
@Entity(
    tableName = "transfer_item",
    foreignKeys = [
        ForeignKey(
            entity = TransferJobEntity::class,
            parentColumns = ["id"],
            childColumns = ["job_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = InstanceEntity::class,
            parentColumns = ["id"],
            childColumns = ["instance_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["job_id"]),
        Index(value = ["instance_id"]),
    ],
)
data class TransferItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "job_id") val jobId: Long,
    @ColumnInfo(name = "instance_id") val instanceId: Long,
    @ColumnInfo(name = "upload_id") val uploadId: String? = null,
    @ColumnInfo(name = "bytes_total") val bytesTotal: Long? = null,
    @ColumnInfo(name = "bytes_sent", defaultValue = "0") val bytesSent: Long = 0,
    val status: TransferItemStatus,
    val error: String? = null,
    @ColumnInfo(name = "saved_path") val savedPath: String? = null,
    @ColumnInfo(name = "finished_at") val finishedAt: Long? = null,
)
