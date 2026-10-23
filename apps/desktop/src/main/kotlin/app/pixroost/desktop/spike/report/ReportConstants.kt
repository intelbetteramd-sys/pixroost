package app.pixroost.desktop.spike.report

object ReportConstants {
    /** The rules of docs/architecture/data-model.md: "same shot" and "similar shots". */
    const val SAME_SHOT_DISTANCE = 4
    const val SIMILAR_DISTANCE = 10
    const val SAME_SHOT_MILLIS = 1_000L
    const val SIMILAR_MILLIS = 60_000L

    /** Width to height ratios closer than this count as the same proportions. */
    const val ASPECT_TOLERANCE = 0.02

    /** Blank-frame thresholds to compare, and how many of the lowest deviations of the other photos to list. */
    val BLANK_THRESHOLDS = listOf(0.01, 0.02, 0.04)
    const val LOWEST_DEVIATIONS = 10

    /** Pairs of different photos within this distance are listed one by one. */
    const val LISTED_PAIR_DISTANCE = 6

    /** Confidence 95 %: with no hit in N trials the share is below 3 / N (the rule of three). */
    const val RULE_OF_THREE = 3

    /** Distances for which false pairs are counted and extrapolated, and the library sizes to extrapolate to. */
    val FALSE_DISTANCES = listOf(2, 3, 4)
    val LIBRARY_SIZES = listOf(10_000, 50_000)

    val THRESHOLDS = listOf(2, 4, 6, 8, 10)
    const val PERCENT = 100.0
    const val PRECISION_SCALE = 10_000.0
    const val MILLIS_IN_SECOND = 1000L
}
