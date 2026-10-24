package app.pixroost.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index

/** One band of the 64-bit pHash, the index for the similar-shot search (LSH). */
@Entity(
    tableName = "phash_band",
    primaryKeys = ["band", "value", "asset_id"],
    foreignKeys = [
        ForeignKey(
            entity = AssetEntity::class,
            parentColumns = ["id"],
            childColumns = ["asset_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["asset_id"]),
    ],
)
data class PhashBandEntity(
    @ColumnInfo(name = "asset_id") val assetId: Long,
    val band: Int,
    val value: Int,
)
