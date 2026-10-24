package app.pixroost.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import app.pixroost.database.model.SourceKind
import app.pixroost.database.model.SourceStatus

/** A connected source: device gallery, a cloud, a folder or a computer archive. */
@Entity(
    tableName = "source",
)
data class SourceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val kind: SourceKind,
    val title: String,
    @ColumnInfo(name = "account_label") val accountLabel: String? = null,
    @ColumnInfo(name = "config_json", defaultValue = "'{}'") val configJson: String = "{}",
    @ColumnInfo(name = "sync_cursor") val syncCursor: String? = null,
    @ColumnInfo(name = "quota_total") val quotaTotal: Long? = null,
    @ColumnInfo(name = "quota_used") val quotaUsed: Long? = null,
    @ColumnInfo(name = "is_trusted", defaultValue = "0") val isTrusted: Boolean = false,
    val status: SourceStatus,
    @ColumnInfo(name = "last_sync_at") val lastSyncAt: Long? = null,
)
