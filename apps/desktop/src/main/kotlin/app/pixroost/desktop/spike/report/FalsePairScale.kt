package app.pixroost.desktop.spike.report

/**
 * How false matches grow with the library: the share of different-photo pairs within each distance by hash alone,
 * times the number of pairs in a library of n photos, n(n-1)/2. For the full rule the sample may hold no false
 * pair at all; then only an upper bound is known, rate < 1 / timed pairs.
 */
fun falsePairScaleLines(counts: DistinctPairCounts): List<String> {
    val byHash = ReportConstants.FALSE_DISTANCES.mapIndexed { index, limit ->
        val found = counts.withinDistance[index]
        "Только pHash ≤ $limit: пар $found, доля ${rateText(found, counts.pairs)}; " +
            "ожидаемо ложных пар: ${expectedText(found, counts.pairs)}"
    }
    val head = "Всё правило (≤ ${ReportConstants.SAME_SHOT_DISTANCE}, 1 с, пропорции): пар ${counts.sameShotRule} " +
        "из ${counts.timedPairs} с датой; "
    val rule = if (counts.sameShotRule == 0) {
        head + "доля < ${rateText(1, counts.timedPairs)} (верхняя оценка); " +
            "ожидаемо ложных пар < ${expectedText(1, counts.timedPairs)}"
    } else {
        head + "доля ${rateText(counts.sameShotRule, counts.timedPairs)}; " +
            "ожидаемо ложных пар: ${expectedText(counts.sameShotRule, counts.timedPairs)}"
    }
    return byHash + rule
}

private fun rateText(count: Int, pairs: Int): String {
    if (pairs == 0 || count == 0) return "0"
    return formatRu("%.2e (1 на %d)", count.toDouble() / pairs, pairs / count)
}

private fun expectedText(count: Int, pairs: Int): String =
    ReportConstants.LIBRARY_SIZES.joinToString(", ") { size ->
        val libraryPairs = size.toDouble() * (size - 1) / 2
        val expected = if (pairs == 0) 0.0 else count.toDouble() / pairs * libraryPairs
        "$size фото — ${formatRu("%.1f", expected)}"
    }
