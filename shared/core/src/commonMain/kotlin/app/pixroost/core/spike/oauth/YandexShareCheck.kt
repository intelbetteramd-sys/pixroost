package app.pixroost.core.spike.oauth

import kotlinx.serialization.json.JsonObject

/** One direct request for a resource: what it was for, the status, and the `share` object if the answer had it. */
data class YandexShareCheck(val kind: String, val path: String, val status: Int, val share: JsonObject?)
