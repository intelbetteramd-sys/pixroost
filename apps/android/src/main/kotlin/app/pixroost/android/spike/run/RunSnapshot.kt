package app.pixroost.android.spike.run

/** Progress of the whole-gallery run, saved after every batch so a killed app still has numbers. */
data class RunSnapshot(
    val total: Int,
    val done: Int,
    val failed: Int,
    val blank: Int,
    val elapsedMs: Long,
    val batteryStart: Int,
    val batteryNow: Int,
    val charging: Boolean,
    val finished: Boolean,
) {
    val photosPerSecond: Double
        get() = if (elapsedMs > 0) done * RunConstants.MILLIS_IN_SECOND / elapsedMs else 0.0

    val photosPerMinute: Double
        get() = if (elapsedMs > 0) done * RunConstants.MILLIS_IN_MINUTE / elapsedMs else 0.0
}
