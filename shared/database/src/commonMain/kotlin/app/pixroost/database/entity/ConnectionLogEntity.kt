package app.pixroost.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey
import app.pixroost.database.model.ConnectionEvent
import app.pixroost.database.model.ConnectionRoute

/** A connection event kept by the computer. */
@Entity(
    tableName = "connection_log",
    indices = [
        Index(value = ["device_id", "at"]),
    ],
)
data class ConnectionLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "device_id") val deviceId: String,
    val at: Long,
    val route: ConnectionRoute,
    val event: ConnectionEvent,
    val details: String? = null,
)
