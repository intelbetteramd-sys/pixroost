package app.pixroost.desktop.spike.report

object ReportConstants {
    /** The rules of docs/architecture/data-model.md: "same shot" and "similar shots". */
    const val SAME_SHOT_DISTANCE = 4
    const val SIMILAR_DISTANCE = 10
    const val SAME_SHOT_MILLIS = 1_000L
    const val SIMILAR_MILLIS = 60_000L

    /** Width to height ratios closer than this count as the same proportions. */
    const val ASPECT_TOLERANCE = 0.02

    val THRESHOLDS = listOf(2, 4, 6, 8, 10)
    const val PERCENT = 100.0
    const val PRECISION_SCALE = 10_000.0
    const val MILLIS_IN_SECOND = 1000L
}
