package app.pixroost.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import app.pixroost.database.model.SimilarGroupKind

/** A group of similar shots. */
@Entity(
    tableName = "similar_group",
)
data class SimilarGroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val kind: SimilarGroupKind,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(defaultValue = "0") val dismissed: Boolean = false,
)
