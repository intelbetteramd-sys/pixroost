package app.pixroost.core.spike.phash

/**
 * With one worse copy per photo there are four frames for two different photos A and B: the originals and the
 * copies. Counts how many of the pairs O_a–O_b, O_a–C_b, C_a–O_b, C_a–C_b are within [limit]. [original] holds the
 * hashes of (A, B), [copy] the hashes of their copies. [allowed] gets whether the first and the second frame of the
 * pair are copies and may reject the pair (for example by proportions).
 */
fun copyCrossMatches(
    original: Pair<Long, Long>,
    copy: Pair<Long, Long>,
    limit: Int,
    allowed: (firstIsCopy: Boolean, secondIsCopy: Boolean) -> Boolean = { _, _ -> true },
): Int {
    val first = listOf(original.first to false, copy.first to true)
    val second = listOf(original.second to false, copy.second to true)
    return first.sumOf { (a, aCopy) ->
        second.count { (b, bCopy) -> hammingDistance(a, b) <= limit && allowed(aCopy, bCopy) }
    }
}
