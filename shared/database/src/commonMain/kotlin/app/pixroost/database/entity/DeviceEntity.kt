package app.pixroost.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import app.pixroost.database.model.DevicePlatform
import app.pixroost.database.model.DeviceRole

/** A paired device. */
@Suppress("ArrayInDataClass") // Never compared by equals; hashes are compared with contentEquals.
@Entity(
    tableName = "device",
)
data class DeviceEntity(
    @PrimaryKey(autoGenerate = true) val id: String,
    val name: String,
    val platform: DevicePlatform,
    val role: DeviceRole,
    @ColumnInfo(name = "public_key") val publicKey: ByteArray,
    @ColumnInfo(name = "transport_key") val transportKey: ByteArray? = null,
    @ColumnInfo(name = "cert_sha256") val certSha256: ByteArray? = null,
    val permissions: String,
    @ColumnInfo(name = "invited_by") val invitedBy: String? = null,
    @ColumnInfo(name = "last_address") val lastAddress: String? = null,
    @ColumnInfo(name = "last_seen_at") val lastSeenAt: Long? = null,
    @ColumnInfo(name = "is_default_archive", defaultValue = "0") val isDefaultArchive: Boolean = false,
    @ColumnInfo(name = "archive_is_trusted", defaultValue = "1") val archiveIsTrusted: Boolean = true,
    @ColumnInfo(name = "paired_at") val pairedAt: Long,
    @ColumnInfo(name = "revoked_at") val revokedAt: Long? = null,
)
