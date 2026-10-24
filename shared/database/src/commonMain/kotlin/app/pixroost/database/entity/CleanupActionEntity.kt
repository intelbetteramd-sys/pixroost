package app.pixroost.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import app.pixroost.database.model.CleanupActionType
import app.pixroost.database.model.CleanupStatus
import app.pixroost.database.model.KeeperKind
import app.pixroost.database.model.SourceKind

/** A cleanup journal entry; it is self-contained and outlives the copies it refers to. */
@Suppress("ArrayInDataClass") // Never compared by equals; hashes are compared with contentEquals.
@Entity(
    tableName = "cleanup_action",
    foreignKeys = [
        ForeignKey(
            entity = InstanceEntity::class,
            parentColumns = ["id"],
            childColumns = ["instance_id"],
            onDelete = ForeignKey.SET_NULL,
        ),
        ForeignKey(
            entity = InstanceEntity::class,
            parentColumns = ["id"],
            childColumns = ["keeper_instance_id"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [
        Index(value = ["plan_id"]),
        Index(value = ["instance_id"]),
        Index(value = ["keeper_instance_id"]),
    ],
)
data class CleanupActionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "plan_id") val planId: String,
    @ColumnInfo(name = "instance_id") val instanceId: Long? = null,
    @ColumnInfo(name = "file_name") val fileName: String? = null,
    @ColumnInfo(name = "size_bytes") val sizeBytes: Long? = null,
    @ColumnInfo(name = "source_kind") val sourceKind: SourceKind,
    @ColumnInfo(name = "source_title") val sourceTitle: String,
    @ColumnInfo(name = "remote_key") val remoteKey: String,
    @ColumnInfo(name = "trash_key") val trashKey: String? = null,
    @ColumnInfo(name = "deleted_sha256") val deletedSha256: ByteArray,
    @ColumnInfo(name = "keeper_kind") val keeperKind: KeeperKind,
    @ColumnInfo(name = "keeper_instance_id") val keeperInstanceId: Long? = null,
    @ColumnInfo(name = "keeper_device_id") val keeperDeviceId: String? = null,
    @ColumnInfo(name = "keeper_title") val keeperTitle: String,
    @ColumnInfo(name = "keeper_sha256") val keeperSha256: ByteArray,
    val action: CleanupActionType,
    val status: CleanupStatus,
    @ColumnInfo(name = "denial_reason") val denialReason: String? = null,
    @ColumnInfo(name = "executed_at") val executedAt: Long? = null,
    @ColumnInfo(name = "trash_expires_at") val trashExpiresAt: Long? = null,
    @ColumnInfo(name = "restored_at") val restoredAt: Long? = null,
    val error: String? = null,
)
