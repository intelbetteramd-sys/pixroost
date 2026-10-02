package app.pixroost.core.spike.phash

/**
 * Whether the SQLite index finds the pair: the hash is split into 4 bands of 16 bits, and only hashes that share
 * at least one band whole are compared. By the pigeonhole rule that guarantees every pair at distance ≤ 3; at 4
 * and above a pair is missed when its differing bits fall into every band.
 */
fun sharesLshBand(a: Long, b: Long): Boolean {
    val bits = PhashConstants.BITS_IN_HASH / PhashConstants.LSH_BANDS
    val mask = (1L shl bits) - 1
    fun band(hash: Long, index: Int) = (hash ushr index * bits) and mask
    return (0 until PhashConstants.LSH_BANDS).any { band(a, it) == band(b, it) }
}
