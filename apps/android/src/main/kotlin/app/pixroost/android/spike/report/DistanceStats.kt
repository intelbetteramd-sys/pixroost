package app.pixroost.android.spike.report

/** Distribution of Hamming distances: how many are 0, up to 2, up to 4, above 4, and the median and the maximum. */
data class DistanceStats(
    val count: Int,
    val zero: Int,
    val upTo2: Int,
    val upTo4: Int,
    val median: Double,
    val max: Int,
) {
    val over4: Int
        get() = count - upTo4

    companion object {
        fun of(values: List<Int>): DistanceStats {
            val sorted = values.sorted()
            val middle = sorted.size / 2
            val median = when {
                sorted.isEmpty() -> 0.0
                sorted.size % 2 == 1 -> sorted[middle].toDouble()
                else -> (sorted[middle - 1] + sorted[middle]) / 2.0
            }
            return DistanceStats(
                count = sorted.size,
                zero = sorted.count { it == 0 },
                upTo2 = sorted.count { it <= 2 },
                upTo4 = sorted.count { it <= ReportConstants.CLOSE },
                median = median,
                max = sorted.maxOrNull() ?: 0,
            )
        }
    }
}
