package app.pixroost.core.spike.oauth

import kotlinx.serialization.json.JsonObject

/** A folder or a file seen while walking the Disk; [share] is the raw `share` object when the API sent one. */
data class YandexShareNode(
    val path: String,
    val isDir: Boolean,
    val depth: Int,
    val parent: String,
    val share: JsonObject?,
)
