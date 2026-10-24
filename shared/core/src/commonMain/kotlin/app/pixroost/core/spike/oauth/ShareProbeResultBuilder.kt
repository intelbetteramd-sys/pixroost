package app.pixroost.core.spike.oauth

/** Counts and anonymised lines for the report; the screen part keeps the real paths and `share` values. */
fun shareProbeResult(
    nodes: List<YandexShareNode>,
    checks: List<YandexShareCheck>,
    disk: YandexDiskClient,
    flat: List<YandexFlatResult>,
): ShareProbeResult {
    val folders = nodes.filter { it.isDir }
    val files = nodes.filterNot { it.isDir }
    val sharedFolders = folders.filter { it.share != null }
    val sharedPrefixes = sharedFolders.map { it.path + "/" }
    val nested = nodes.filter { node -> sharedPrefixes.any { node.path.startsWith(it) } }
    val summary = buildList {
        add("просмотрено папок ${folders.size}, файлов ${files.size} (не глубже ${YandexShareConstants.MAX_DEPTH})")
        add("папок с share: ${sharedFolders.size}; файлов с share: ${files.count { it.share != null }}")
        add("значения share: " + valueCounts(nodes))
        add("внутри папок с share: ${nestedLine(nested)}")
        checks.groupBy { it.kind }.forEach { (kind, group) ->
            add("$kind: share есть в ${group.count { it.share != null }} из ${group.size}")
        }
        addAll(flatLines(flat))
        addAll(publishedLines(nodes))
        addAll(rootLines(nodes, checks))
        add("неизвестные ключи ресурса: " + disk.unknownKeys.sorted().ifEmpty { listOf("нет") }.joinToString())
        disk.statuses.forEach { (kind, codes) ->
            add("HTTP, $kind: " + codes.entries.joinToString { "${it.key}×${it.value}" })
        }
    }
    return ShareProbeResult(summary, rootScreenLines(nodes) + screenLines(nodes, checks))
}

private fun valueCounts(nodes: List<YandexShareNode>): String {
    val counts = nodes.mapNotNull { it.share }.groupingBy { anonymisedShare(it) }.eachCount()
    return counts.entries.joinToString("; ") { "{${it.key}}×${it.value}" }.ifEmpty { "нет" }
}

private fun nestedLine(nested: List<YandexShareNode>): String {
    val subfolders = nested.filter { it.isDir }
    val files = nested.filterNot { it.isDir }
    return "подпапок ${subfolders.size}, из них с share ${subfolders.count { it.share != null }}; " +
        "файлов ${files.size}, из них с share ${files.count { it.share != null }}"
}

private fun screenLines(nodes: List<YandexShareNode>, checks: List<YandexShareCheck>): List<String> {
    val found = nodes.filter { it.share != null }.map {
        (if (it.isDir) "папка " else "файл ") + "${it.path}: share=${it.share}"
    }
    val direct = checks.map { "${it.kind} ${it.path}: HTTP ${it.status}, share=${it.share ?: "нет"}" }
    return (direct + found.ifEmpty { listOf("ни у одной папки и файла share нет") })
        .take(YandexShareConstants.MAX_SCREEN_LINES)
}
