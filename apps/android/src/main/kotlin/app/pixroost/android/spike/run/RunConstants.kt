package app.pixroost.android.spike.run

object RunConstants {
    /** Photos hashed at the same time: enough to fill the cores, few enough to keep the phone cool. */
    const val PARALLELISM = 3

    /** Photos per batch; progress is shown and saved after each one. */
    const val BATCH = 12

    const val CHECK_LIMIT = 500
    const val HEIC_LIMIT = 100
    const val MILLIS_IN_MINUTE = 60_000.0
    const val MILLIS_IN_SECOND = 1000.0
}
