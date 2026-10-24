package app.pixroost.core.spike.oauth

import kotlinx.serialization.json.JsonObject

/** The HTTP status of one Disk call and its JSON body when the call succeeded. */
data class YandexReply(val status: Int, val body: JsonObject?)
