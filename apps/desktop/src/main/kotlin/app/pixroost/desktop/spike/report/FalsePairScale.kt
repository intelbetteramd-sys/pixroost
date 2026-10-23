package app.pixroost.desktop.spike.report

import app.pixroost.core.spike.phash.PhashConstants
import app.pixroost.core.spike.phash.linearScale
import app.pixroost.core.spike.phash.quadraticScale
import app.pixroost.core.spike.phash.ruleOfThreeBound

/**
 * How false matches grow with the library. Pairs shot close in time (within 1 s for the full rule, within 60 s for
 * the "similar" rule and for the 60 s part of the hash-only lines) come from one burst of shots, so their number
 * grows linearly with the library: count × n / N of the sample. The remaining pairs of the hash-only lines are
 * random coincidences and grow with the number of pairs: rate × n(n-1)/2. The full rule may show no false pair at
 * all in the sample; then the rule of three gives the bound: fewer than 3 for the sample, < 3/N per photo, and
 * < 3 × n / N for a library of n photos, with 95 % confidence.
 */
fun falsePairScaleLines(counts: DistinctPairCounts): List<String> {
    val byHash = ReportConstants.FALSE_DISTANCES.mapIndexed { index, limit ->
        val found = counts.withinDistance[index]
        val burst = counts.withinDistanceBurst[index]
        val rest = found - burst
        "Только pHash ≤ $limit: пар $found. Снятых в пределах 60 с: $burst, в библиотеке " +
            "(линейно): ${linearText(burst, counts.photos)}. Остальные: $rest, " +
            "доля ${rateText(rest, counts.pairs)}, в библиотеке (квадратично): ${quadraticText(rest, counts.pairs)}"
    }
    val head = "Всё правило (≤ ${ReportConstants.SAME_SHOT_DISTANCE}, 1 с, пропорции): "
    val rule = if (counts.sameShotRule == 0) {
        val three = PhashConstants.RULE_OF_THREE.toInt()
        head + "0 ложных пар на ${counts.photos} фото; с 95 % уверенностью < $three/${counts.photos} на фото; " +
            "в библиотеке (линейно): ${boundText(counts.photos)}"
    } else {
        head + "ложных пар ${counts.sameShotRule} на ${counts.photos} фото; в библиотеке (линейно): " +
            linearText(counts.sameShotRule, counts.photos)
    }
    val similar = "pHash ≤ ${ReportConstants.SIMILAR_DISTANCE} и 60 с (правило «похожие кадры»): " +
        "пар ${counts.similarRule}; в библиотеке (линейно): ${linearText(counts.similarRule, counts.photos)}"
    return byHash + rule + similar
}

private fun rateText(count: Int, pairs: Int): String {
    if (pairs == 0 || count == 0) return "0"
    return formatRu("%.2e (1 на %d)", count.toDouble() / pairs, pairs / count)
}

private fun linearText(count: Int, photos: Int): String = ReportConstants.LIBRARY_SIZES.joinToString(", ") { size ->
    "$size фото — ${formatRu("%.1f", linearScale(count, photos, size))}"
}

private fun quadraticText(count: Int, pairs: Int): String = ReportConstants.LIBRARY_SIZES.joinToString(", ") { size ->
    "$size фото — ${formatRu("%.1f", quadraticScale(count, pairs, size))}"
}

private fun boundText(photos: Int): String = ReportConstants.LIBRARY_SIZES.joinToString(", ") { size ->
    "$size фото — < ${formatRu("%.1f", ruleOfThreeBound(photos, size))}"
}
