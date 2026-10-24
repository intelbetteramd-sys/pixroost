package app.pixroost.core.spike.oauth

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

/** Reads the flat file list the connector will index with and checks whether it carries `share`. */
class YandexFlatListProbe(private val disk: YandexDiskClient) {
    suspend fun check(nodes: List<YandexShareNode>, kind: String, mediaType: String?): YandexFlatResult {
        val prefixes = nodes.filter { it.isDir && it.share != null }.map { it.path + "/" }
        val wanted = nodes.filter { node -> !node.isDir && prefixes.any { node.path.startsWith(it) } }
            .map { it.path }.toSet()
        var listed = 0
        var found = 0
        var withShare = 0
        var offset = 0
        var pages = 0
        do {
            val reply = disk.flatPage(kind, mediaType, offset)
            val items = (reply.body?.get("items") as? JsonArray).orEmpty().mapNotNull { it as? JsonObject }
            for (item in items) {
                disk.noteKeys(item)
                if ((item["path"] as? JsonPrimitive)?.contentOrNull in wanted) {
                    found++
                    if (item[YandexShareConstants.SHARE_KEY] is JsonObject) withShare++
                }
            }
            listed += items.size
            offset += YandexShareConstants.FLAT_PAGE_SIZE
            pages++
        } while (items.size == YandexShareConstants.FLAT_PAGE_SIZE && pages < YandexShareConstants.MAX_FLAT_PAGES)
        return YandexFlatResult(kind, listed, wanted.size, found, withShare)
    }
}
