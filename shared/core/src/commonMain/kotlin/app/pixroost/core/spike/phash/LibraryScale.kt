package app.pixroost.core.spike.phash

/** A [count] seen in a sample of [sampleSize] photos, for a library of [librarySize]: pairs that grow linearly. */
fun linearScale(count: Int, sampleSize: Int, librarySize: Int): Double =
    if (sampleSize == 0) 0.0 else count.toDouble() * librarySize / sampleSize

/** A [count] among [samplePairs] pairs, for a library of [librarySize]: pairs that grow as n(n-1)/2. */
fun quadraticScale(count: Int, samplePairs: Int, librarySize: Int): Double {
    if (samplePairs == 0) return 0.0
    return count.toDouble() / samplePairs * librarySize * (librarySize - 1.0) / 2
}

/** With no hit in a sample of [sampleSize], the linearly scaled count is below this at 95 % confidence. */
fun ruleOfThreeBound(sampleSize: Int, librarySize: Int): Double =
    if (sampleSize == 0) 0.0 else PhashConstants.RULE_OF_THREE * librarySize / sampleSize
