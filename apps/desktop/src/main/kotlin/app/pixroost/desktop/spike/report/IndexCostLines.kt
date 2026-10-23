package app.pixroost.desktop.spike.report

/** The cost of the index: the share of random pairs of different photos that become candidates to compare. */
fun indexCostLines(counts: DistinctPairCounts): List<String> = listOf(
    "Индекс, 5 полос: случайных пар в кандидатах ${counts.fiveBandCandidates} " +
        "(${percentPrecise(counts.fiveBandCandidates, counts.pairs)}); для сравнения 4 полосы: " +
        "${counts.fourBandCandidates} (${percentPrecise(counts.fourBandCandidates, counts.pairs)})",
)
