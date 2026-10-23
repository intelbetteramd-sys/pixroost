package app.pixroost.desktop.spike.report

/**
 * How false matches grow with the library: the share of different-photo pairs within each distance by hash alone,
 * times the number of pairs in a library of n photos, n(n-1)/2. The full rule may show no false pair at all in the
 * sample; then the rule of three gives the bound: 0 of N, share < 3 / N with 95 % confidence.
 */
fun falsePairScaleLines(counts: DistinctPairCounts): List<String> {
    val byHash = ReportConstants.FALSE_DISTANCES.mapIndexed { index, limit ->
        val found = counts.withinDistance[index]
        "Только pHash ≤ $limit: пар $found, доля ${rateText(found, counts.pairs)}; " +
            "ожидаемо ложных пар: ${expectedText(found, counts.pairs)}"
    }
    val head = "Всё правило (≤ ${ReportConstants.SAME_SHOT_DISTANCE}, 1 с, пропорции): "
    val rule = if (counts.sameShotRule == 0) {
        head + "0 из ${counts.timedPairs}, с 95 % уверенностью доля < " +
            "${ReportConstants.RULE_OF_THREE}/${counts.timedPairs}"
    } else {
        head + "пар ${counts.sameShotRule} из ${counts.timedPairs} с датой; " +
            "доля ${rateText(counts.sameShotRule, counts.timedPairs)}; " +
            "ожидаемо ложных пар: ${expectedText(counts.sameShotRule, counts.timedPairs)}"
    }
    return byHash + rule
}

private fun rateText(count: Int, pairs: Int): String {
    if (pairs == 0 || count == 0) return "0"
    return formatRu("%.2e (1 на %d)", count.toDouble() / pairs, pairs / count)
}

private fun expectedText(count: Int, pairs: Int): String = ReportConstants.LIBRARY_SIZES.joinToString(", ") { size ->
    val libraryPairs = size.toDouble() * (size - 1) / 2
    val expected = if (pairs == 0) 0.0 else count.toDouble() / pairs * libraryPairs
    "$size фото — ${formatRu("%.1f", expected)}"
}
