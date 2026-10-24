package app.pixroost.android.spike.run

/** Runs [block] and returns its result with the time it took in nanoseconds. */
inline fun <T> timed(block: () -> T): Pair<T, Long> {
    val start = System.nanoTime()
    val result = block()
    return result to (System.nanoTime() - start)
}
