package app.pixroost.core.spike.oauth

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject

/**
 * Reads the first page of the user's files and counts which content hashes the service returns without a download:
 * exact duplicates are found by these hashes. Only counts go to the screen and the report, no file names.
 */
class CloudMediaSample(private val http: HttpClient) {
    suspend fun sample(service: CloudService, accessToken: String): String = when (service) {
        CloudService.Yandex -> yandex(accessToken)
        CloudService.Dropbox -> dropbox(accessToken)
        CloudService.Microsoft -> microsoft(accessToken)
        CloudService.Google -> "в Google Фото списка нет, только выбор"
    }

    private suspend fun yandex(token: String): String {
        val page = json(
            http.get("https://cloud-api.yandex.net/v1/disk/resources/files") {
                header("Authorization", "OAuth $token")
                parameter("media_type", "image,video")
                parameter("limit", OAuthSpikeConstants.SAMPLE_SIZE)
            },
        )
        val files = page.objects("items")
        return summary(
            files.size,
            "MD5" to files.count { it.text("md5") != null },
            "SHA-256" to files.count { it.text("sha256") != null },
        )
    }

    private suspend fun dropbox(token: String): String {
        val page = json(
            http.post("https://api.dropboxapi.com/2/files/list_folder") {
                bearer(token)
                contentType(ContentType.Application.Json)
                setBody("""{"path":"","recursive":true,"limit":${OAuthSpikeConstants.SAMPLE_SIZE}}""")
            },
        )
        val files = page.objects("entries").filter { it.text(".tag") == "file" }
        return summary(files.size, "content_hash" to files.count { it.text("content_hash") != null })
    }

    private suspend fun microsoft(token: String): String {
        val page = json(
            http.get("https://graph.microsoft.com/v1.0/me/drive/root/delta") {
                bearer(token)
                parameter("\$top", OAuthSpikeConstants.SAMPLE_SIZE)
            },
        )
        val hashes = page.objects("value").mapNotNull { (it["file"] as? JsonObject)?.get("hashes") as? JsonObject }
        return summary(
            hashes.size,
            "quickXorHash" to hashes.count { it.text("quickXorHash") != null },
            "SHA-1" to hashes.count { it.text("sha1Hash") != null },
            "SHA-256" to hashes.count { it.text("sha256Hash") != null },
        )
    }

    private fun summary(files: Int, vararg hashes: Pair<String, Int>): String =
        "файлов в выборке $files; хэши: " + hashes.joinToString { (name, count) -> "$name у $count" }

    private suspend fun json(response: HttpResponse): JsonObject {
        val body = response.bodyAsText()
        if (!response.status.isSuccess()) {
            throw OAuthException("${response.status.value}: ${body.take(OAuthSpikeConstants.ERROR_BODY_CHARS)}")
        }
        return OAuthJson.parse(body).jsonObject
    }

    private fun HttpRequestBuilder.bearer(token: String) = header("Authorization", "Bearer $token")

    private fun JsonObject.objects(key: String): List<JsonObject> =
        (this[key] as? JsonArray).orEmpty().mapNotNull { it as? JsonObject }

    private fun JsonObject.text(key: String): String? = (this[key] as? JsonPrimitive)?.contentOrNull
}
