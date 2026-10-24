package app.pixroost.core.spike.oauth

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.JsonObject

/** Read-only GET calls to the Disk API that remember every HTTP status and every undocumented key. */
class YandexDiskClient(private val http: HttpClient, private val token: String) {
    /** Call kind to HTTP status to number of calls. */
    val statuses = mutableMapOf<String, MutableMap<Int, Int>>()
    val unknownKeys = mutableSetOf<String>()

    /** Asks for all fields unless [fields] is given: then the service may filter `share` out. */
    suspend fun resource(
        kind: String,
        path: String,
        limit: Int? = null,
        offset: Int? = null,
        fields: String? = null,
    ): YandexReply {
        val reply = get(kind, YandexShareConstants.RESOURCES_URL) {
            parameter("path", path)
            parameter("limit", limit)
            parameter("offset", offset)
            parameter("fields", fields)
        }
        reply.body?.let(::noteKeys)
        return reply
    }

    /** One page of the flat file list `/v1/disk/resources/files`; [mediaType] null means no filter. */
    suspend fun flatPage(kind: String, mediaType: String?, offset: Int): YandexReply =
        get(kind, YandexShareConstants.FLAT_URL) {
            parameter("limit", YandexShareConstants.FLAT_PAGE_SIZE)
            parameter("offset", offset)
            parameter("media_type", mediaType)
        }

    fun noteKeys(resource: JsonObject) {
        unknownKeys += resource.keys.filter {
            it !in YandexShareConstants.DOCUMENTED_KEYS && it != YandexShareConstants.SHARE_KEY
        }
    }

    private suspend fun get(kind: String, url: String, params: HttpRequestBuilder.() -> Unit): YandexReply {
        val response = http.get(url) {
            header("Authorization", "OAuth $token")
            params()
        }
        val code = response.status.value
        val counts = statuses.getOrPut(kind) { mutableMapOf() }
        counts[code] = (counts[code] ?: 0) + 1
        val body = response.bodyAsText()
        if (!response.status.isSuccess()) return YandexReply(code, null)
        return YandexReply(code, OAuthJson.parse(body) as? JsonObject)
    }
}
