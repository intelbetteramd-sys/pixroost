package app.pixroost.core.spike.phash

private val offsetPattern = Regex("""([+-])(\d{2}):(\d{2})""")

/** Seconds east of UTC from an EXIF `OffsetTimeOriginal` value such as `+03:00`; `null` when it is absent or odd. */
fun parseOffsetSeconds(text: String?): Int? {
    val match = text?.trim()?.let { offsetPattern.matchEntire(it) } ?: return null
    val (sign, hours, minutes) = match.destructured
    val seconds = (hours.toInt() * PhashConstants.MINUTES_IN_HOUR + minutes.toInt()) * PhashConstants.SECONDS_IN_MINUTE
    return if (sign == "-") -seconds else seconds
}

/**
 * Shot times of a folder on one scale, in milliseconds. A photo with an offset is moved to UTC by it. A photo
 * without one is assumed to have the offset most common among the photos of the folder that have one: the same
 * phone and the same place, so the mixed folder stays on one clock and does not shift by hours (a fixed guess such
 * as the computer's zone would shift the photos with an offset against those without). When no photo has an offset
 * all times stay as wall clock, which is consistent by itself.
 */
fun resolveShotTimes(times: List<WallTime?>): List<Long?> {
    val assumed = times.mapNotNull { it?.offsetSeconds }
        .groupingBy { it }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key ?: 0
    return times.map { time ->
        time?.let { it.wallMillis - (it.offsetSeconds ?: assumed) * PhashConstants.MILLIS_IN_SECOND }
    }
}
