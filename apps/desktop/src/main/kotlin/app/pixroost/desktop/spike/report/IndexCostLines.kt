package app.pixroost.desktop.spike.report

/** The cost of the index: the share of random pairs of different photos that become candidates to compare. */
fun indexCostLines(counts: DistinctPairCounts): List<String> = listOf(
    "Индекс, случайные пары разных фото в кандидатах: 4 полосы " +
        "${counts.fourBandCandidates} (${percentPrecise(counts.fourBandCandidates, counts.pairs)}), 5 полос " +
        "${counts.fiveBandCandidates} (${percentPrecise(counts.fiveBandCandidates, counts.pairs)})",
)
