package app.pixroost.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import app.pixroost.database.model.TransferJobKind
import app.pixroost.database.model.TransferJobStatus

/** A queued transfer of files to a device. */
@Entity(
    tableName = "transfer_job",
    foreignKeys = [
        ForeignKey(
            entity = DeviceEntity::class,
            parentColumns = ["id"],
            childColumns = ["device_id"],
            onDelete = ForeignKey.NO_ACTION,
        ),
    ],
    indices = [
        Index(value = ["device_id"]),
    ],
)
data class TransferJobEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "device_id") val deviceId: String,
    val kind: TransferJobKind,
    val status: TransferJobStatus,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
