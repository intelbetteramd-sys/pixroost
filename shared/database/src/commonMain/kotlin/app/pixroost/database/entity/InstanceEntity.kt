package app.pixroost.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import app.pixroost.database.model.InstanceQuality
import app.pixroost.database.model.InstanceState

/** A physical copy of an asset in one source. */
@Suppress("ArrayInDataClass") // Never compared by equals; hashes are compared with contentEquals.
@Entity(
    tableName = "instance",
    foreignKeys = [
        ForeignKey(
            entity = AssetEntity::class,
            parentColumns = ["id"],
            childColumns = ["asset_id"],
            onDelete = ForeignKey.NO_ACTION,
        ),
        ForeignKey(
            entity = SourceEntity::class,
            parentColumns = ["id"],
            childColumns = ["source_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["source_id", "remote_key"], unique = true),
        Index(name = "instance_asset", value = ["asset_id"]),
        Index(name = "instance_sha256", value = ["sha256"]),
        Index(name = "instance_md5", value = ["md5"]),
    ],
)
data class InstanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "asset_id") val assetId: Long,
    @ColumnInfo(name = "source_id") val sourceId: Long,
    @ColumnInfo(name = "remote_key") val remoteKey: String,
    @ColumnInfo(name = "file_id") val fileId: String? = null,
    @ColumnInfo(name = "file_name") val fileName: String? = null,
    @ColumnInfo(name = "mime_type") val mimeType: String? = null,
    @ColumnInfo(name = "size_bytes") val sizeBytes: Long? = null,
    val md5: ByteArray? = null,
    val sha256: ByteArray? = null,
    @ColumnInfo(name = "provider_hash") val providerHash: String? = null,
    val quality: InstanceQuality,
    @ColumnInfo(name = "modified_at") val modifiedAt: Long? = null,
    val revision: String? = null,
    @ColumnInfo(name = "hashed_revision") val hashedRevision: String? = null,
    @ColumnInfo(name = "in_shared_folder") val inSharedFolder: Boolean? = null,
    val state: InstanceState,
    @ColumnInfo(name = "verified_at") val verifiedAt: Long? = null,
    @ColumnInfo(name = "last_seen_at") val lastSeenAt: Long,
)
