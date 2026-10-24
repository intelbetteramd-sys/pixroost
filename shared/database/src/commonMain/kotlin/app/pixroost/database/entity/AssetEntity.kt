package app.pixroost.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey
import app.pixroost.database.model.MediaType

/** A logical photo or video; its physical copies are [InstanceEntity] rows. */
@Entity(
    tableName = "asset",
    indices = [
        Index(name = "asset_taken_at", value = ["taken_at"], orders = [Index.Order.DESC]),
    ],
)
data class AssetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "media_type") val mediaType: MediaType,
    @ColumnInfo(name = "taken_at") val takenAt: Long? = null,
    @ColumnInfo(name = "tz_offset_min") val tzOffsetMin: Int? = null,
    val width: Int? = null,
    val height: Int? = null,
    @ColumnInfo(name = "duration_ms") val durationMs: Long? = null,
    val phash: Long? = null,
    @ColumnInfo(name = "is_blank", defaultValue = "0") val isBlank: Boolean = false,
    @ColumnInfo(name = "best_instance_id") val bestInstanceId: Long? = null,
)
