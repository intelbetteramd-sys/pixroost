package app.pixroost.core.spike.oauth

import io.ktor.client.HttpClient
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

/**
 * Looks for the undocumented `share` field of the Yandex Disk resource: walks the folders breadth-first, notes which
 * resources carry it, then asks for some of them directly. GET only, nothing is changed on the Disk.
 */
class YandexShareProbe(private val http: HttpClient) {
    suspend fun probe(accessToken: String): ShareProbeResult {
        val disk = YandexDiskClient(http, accessToken)
        val nodes = walk(disk)
        val checks = directChecks(disk, nodes)
        val flatProbe = YandexFlatListProbe(disk)
        val flat = listOf(
            flatProbe.check(nodes, YandexShareConstants.KIND_FLAT_MEDIA, YandexShareConstants.MEDIA_TYPES),
            flatProbe.check(nodes, YandexShareConstants.KIND_FLAT_ALL, null),
        )
        return shareProbeResult(nodes, checks, disk, flat)
    }

    private suspend fun walk(disk: YandexDiskClient): List<YandexShareNode> {
        val nodes = mutableListOf<YandexShareNode>()
        val queue = ArrayDeque<YandexShareNode>()
        queue.add(YandexShareNode(YandexShareConstants.ROOT, isDir = true, depth = 0, parent = "", share = null))
        var listed = 0
        while (queue.isNotEmpty() && listed < YandexShareConstants.MAX_FOLDERS) {
            val folder = queue.removeFirst()
            listed++
            for (item in listFolder(disk, folder.path)) {
                val node = nodeOf(disk, item, folder)
                nodes.add(node)
                if (node.isDir && node.depth < YandexShareConstants.MAX_DEPTH) queue.add(node)
            }
        }
        return nodes
    }

    private suspend fun listFolder(disk: YandexDiskClient, path: String): List<JsonObject> {
        val items = mutableListOf<JsonObject>()
        var offset = 0
        var pages = 0
        do {
            val reply = disk.resource(YandexShareConstants.KIND_LISTING, path, YandexShareConstants.PAGE_SIZE, offset)
            val chunk = ((reply.body?.get("_embedded") as? JsonObject)?.get("items") as? JsonArray)
                .orEmpty()
                .mapNotNull { it as? JsonObject }
            items += chunk
            offset += YandexShareConstants.PAGE_SIZE
            pages++
        } while (chunk.size == YandexShareConstants.PAGE_SIZE && pages < YandexShareConstants.MAX_PAGES_PER_FOLDER)
        return items
    }

    private fun nodeOf(disk: YandexDiskClient, item: JsonObject, parent: YandexShareNode): YandexShareNode {
        disk.noteKeys(item)
        return YandexShareNode(
            path = (item["path"] as? JsonPrimitive)?.contentOrNull.orEmpty(),
            isDir = (item["type"] as? JsonPrimitive)?.contentOrNull == "dir",
            depth = parent.depth + 1,
            parent = parent.path,
            share = item[YandexShareConstants.SHARE_KEY] as? JsonObject,
            resourceId = (item["resource_id"] as? JsonPrimitive)?.contentOrNull,
            published = "public_key" in item || "public_url" in item,
        )
    }

    /** Each shared folder, a subfolder and a file inside it, and one plain folder as a control. */
    private suspend fun directChecks(disk: YandexDiskClient, nodes: List<YandexShareNode>): List<YandexShareCheck> {
        val targets = mutableListOf<Pair<String, String>>()
        val shared = nodes.filter { it.isDir && it.share != null }.take(YandexShareConstants.MAX_DIRECT_FOLDERS)
        for (folder in shared) {
            targets += YandexShareConstants.KIND_SHARED to folder.path
            val children = nodes.filter { it.parent == folder.path }
            children.firstOrNull { it.isDir }?.let { targets += YandexShareConstants.KIND_SUBFOLDER to it.path }
            children.firstOrNull { !it.isDir }?.let { targets += YandexShareConstants.KIND_FILE to it.path }
        }
        val control = nodes.firstOrNull { it.isDir && it.share == null }
        if (control != null) targets += YandexShareConstants.KIND_CONTROL to control.path
        val plain = targets.map { (kind, path) -> check(disk, kind, path, null) }
        val filtered = targets.map { (kind, path) ->
            check(disk, YandexShareConstants.KIND_FIELDS + kind, path, YandexShareConstants.FIELDS_WITHOUT_SHARE)
        }
        return plain + filtered
    }

    private suspend fun check(disk: YandexDiskClient, kind: String, path: String, fields: String?): YandexShareCheck {
        val reply = disk.resource(kind, path, fields = fields)
        return YandexShareCheck(
            kind = kind,
            path = path,
            status = reply.status,
            share = reply.body?.get(YandexShareConstants.SHARE_KEY) as? JsonObject,
            resourceId = (reply.body?.get("resource_id") as? JsonPrimitive)?.contentOrNull,
        )
    }
}
