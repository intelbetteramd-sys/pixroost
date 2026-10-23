package app.pixroost.core.spike.phash

/** A shot time as the file records it: the wall clock read as if it were UTC, and the UTC offset when recorded. */
data class WallTime(val wallMillis: Long, val offsetSeconds: Int?)
