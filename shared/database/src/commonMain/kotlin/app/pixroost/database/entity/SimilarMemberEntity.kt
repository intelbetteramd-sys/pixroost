package app.pixroost.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index

/** A member of a [SimilarGroupEntity]. */
@Entity(
    tableName = "similar_member",
    primaryKeys = ["group_id", "asset_id"],
    foreignKeys = [
        ForeignKey(
            entity = SimilarGroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["group_id"],
            onDelete = ForeignKey.CASCADE,
        ),
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
data class SimilarMemberEntity(
    @ColumnInfo(name = "group_id") val groupId: Long,
    @ColumnInfo(name = "asset_id") val assetId: Long,
    val distance: Int? = null,
)
