package app.pixroost.core.spike.oauth

import kotlinx.serialization.json.JsonObject

/** A folder or a file seen while walking the Disk; [share] is the raw `share` object when the API sent one. */
data class YandexShareNode(
    val path: String,
    val isDir: Boolean,
    val depth: Int,
    val parent: String,
    val share: JsonObject?,
    val resourceId: String? = null,
    /** The item has `public_key` or `public_url`: published by link, which is not the same as shared. */
    val published: Boolean = false,
)
