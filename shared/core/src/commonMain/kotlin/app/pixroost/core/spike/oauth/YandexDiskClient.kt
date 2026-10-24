package app.pixroost.core.spike.oauth

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.JsonObject

/** Read-only GET calls to `/v1/disk/resources` that remember every HTTP status and every undocumented key. */
class YandexDiskClient(private val http: HttpClient, private val token: String) {
    /** Call kind to HTTP status to number of calls. */
    val statuses = mutableMapOf<String, MutableMap<Int, Int>>()
    val unknownKeys = mutableSetOf<String>()

    /** Asks for all fields: `fields` is not passed, so the service filters nothing out. */
    suspend fun resource(kind: String, path: String, limit: Int? = null, offset: Int? = null): YandexReply {
        val response = http.get(YandexShareConstants.RESOURCES_URL) {
            header("Authorization", "OAuth $token")
            parameter("path", path)
            parameter("limit", limit)
            parameter("offset", offset)
        }
        val code = response.status.value
        val counts = statuses.getOrPut(kind) { mutableMapOf() }
        counts[code] = (counts[code] ?: 0) + 1
        val body = response.bodyAsText()
        if (!response.status.isSuccess()) return YandexReply(code, null)
        val json = OAuthJson.parse(body) as? JsonObject
        json?.let(::noteKeys)
        return YandexReply(code, json)
    }

    fun noteKeys(resource: JsonObject) {
        unknownKeys += resource.keys.filter {
            it !in YandexShareConstants.DOCUMENTED_KEYS && it != YandexShareConstants.SHARE_KEY
        }
    }
}
