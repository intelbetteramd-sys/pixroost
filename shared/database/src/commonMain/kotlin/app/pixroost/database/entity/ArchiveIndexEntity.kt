package app.pixroost.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey

/** Phone-side cache of the files already in the archive of a computer. */
@Suppress("ArrayInDataClass") // Never compared by equals; hashes are compared with contentEquals.
@Entity(
    tableName = "archive_index",
    primaryKeys = ["device_id", "sha256"],
    foreignKeys = [
        ForeignKey(
            entity = DeviceEntity::class,
            parentColumns = ["id"],
            childColumns = ["device_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class ArchiveIndexEntity(
    @ColumnInfo(name = "device_id") val deviceId: String,
    val sha256: ByteArray,
    @ColumnInfo(name = "saved_path") val savedPath: String? = null,
)
