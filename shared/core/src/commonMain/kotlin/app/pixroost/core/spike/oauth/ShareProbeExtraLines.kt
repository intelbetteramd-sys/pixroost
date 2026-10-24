package app.pixroost.core.spike.oauth

import kotlinx.serialization.json.JsonPrimitive

/** Report lines about the flat list. */
fun flatLines(flat: List<YandexFlatResult>): List<String> = flat.map {
    "${it.kind}: просмотрено ${it.listed}; файлов из общих папок ${it.sharedFiles}, найдено в списке ${it.found}, " +
        "из них с share ${it.withShare}, без share ${it.found - it.withShare}"
}

/** Report lines about items published by link (`public_key`, `public_url`), which are not the same as shared. */
fun publishedLines(nodes: List<YandexShareNode>): List<String> {
    val published = nodes.filter { it.published }
    return listOf(
        "опубликовано по ссылке: папок ${published.count { it.isDir }}, файлов ${published.count { !it.isDir }}; " +
            "из них с share ${published.count { it.share != null }}",
        "с share и без публикации: ${nodes.count { it.share != null && !it.published }}",
    )
}

/** Report lines about how the root of a shared folder is identified; the ids themselves go to the screen only. */
fun rootLines(nodes: List<YandexShareNode>, checks: List<YandexShareCheck>): List<String> {
    val roots = sharedRoots(nodes)
    val direct = checks.filter { it.kind == YandexShareConstants.KIND_SHARED }.associateBy { it.path }
    val same = roots.count { it.resourceId != null && it.resourceId == direct[it.path]?.resourceId }
    return listOf(
        "корней общих папок (is_root=true): ${roots.size}; " +
            "с resource_id в листинге ${roots.count { it.resourceId != null }}",
        "resource_id корня совпал в листинге и в прямом запросе: $same из ${roots.size}",
    )
}

/** Screen-only lines: the real identifiers of the shared roots. */
fun rootScreenLines(nodes: List<YandexShareNode>): List<String> =
    sharedRoots(nodes).map { "корень ${it.path}: resource_id=${it.resourceId ?: "нет"}" }

private fun sharedRoots(nodes: List<YandexShareNode>): List<YandexShareNode> =
    nodes.filter { it.isDir && (it.share?.get("is_root") as? JsonPrimitive)?.content == "true" }
