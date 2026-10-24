package app.pixroost.android.spike.report

import app.pixroost.android.spike.run.PairSample
import app.pixroost.android.spike.run.VariantResult

/** Distance distribution and time of every candidate fix, on all photos and on the ones that mismatch today. */
fun variantLines(pairs: List<PairSample>): List<String> {
    val bad = pairs.filter { it.distance > ReportConstants.CLOSE }
    val rows = listOf<Pair<String, (PairSample) -> VariantResult>>(
        "0 как есть, loadThumbnail 512×512" to { it.variants.asIs },
        "a превью в пропорциях оригинала" to { it.variants.aspect },
        "b превью без полос по краям" to { it.variants.trimmed },
        "c оригинал, обрезанный по центру до пропорций превью (расстояние до превью)" to { it.variants.cropped },
        "d старое превью MediaStore.Images.Thumbnails MINI_KIND" to { it.variants.legacy },
        "e оригинал, декодированный до ~512 px" to { it.variants.decoded },
    )
    val lines = mutableListOf("== Кандидаты на исправление (расстояние до оригинала, если не сказано иное)")
    for ((name, pick) in rows) {
        val all = pairs.map(pick)
        lines += "$name: " + DistanceStats.of(all.mapNotNull { it.distance }).line() +
            "; нет результата ${all.count { it.distance == null }}; ${millis(all)} мс/фото" +
            (all.count { it.changed }.takeIf { it > 0 }?.let { "; изменено у $it" } ?: "")
        lines += "  только у ${bad.size} расхождений «как есть»: " +
            DistanceStats.of(bad.mapNotNull { pick(it).distance }).line()
    }
    lines += "Время: загрузка и хеш в потоке из 3 параллельных задач; у b и c без загрузки самой картинки"
    return lines
}

private fun millis(results: List<VariantResult>): String =
    number(if (results.isEmpty()) 0.0 else results.sumOf { it.nanos } / ReportConstants.NANOS_IN_MILLI / results.size)
