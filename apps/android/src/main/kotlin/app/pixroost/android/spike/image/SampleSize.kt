package app.pixroost.android.spike.image

/** The largest power of two that keeps [longSide] divided by it at or above [targetSide]; 1 for a full decode. */
internal fun sampleSizeFor(longSide: Int, targetSide: Int?): Int {
    var sample = 1
    while (targetSide != null && longSide / (sample * 2) >= targetSide) sample *= 2
    return sample
}
