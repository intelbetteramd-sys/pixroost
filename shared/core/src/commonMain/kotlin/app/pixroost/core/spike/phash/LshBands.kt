package app.pixroost.core.spike.phash

private val fourBandWidths = List(PhashConstants.LSH_BANDS) { PhashConstants.BITS_IN_HASH / PhashConstants.LSH_BANDS }

/**
 * Whether the SQLite index finds the pair: the hash is split into 4 bands of 16 bits, and only hashes that share
 * at least one band whole are compared. By the pigeonhole rule that guarantees every pair at distance ≤ 3; at 4
 * and above a pair is missed when its differing bits fall into every band.
 */
fun sharesLshBand(a: Long, b: Long): Boolean = sharesBand(a, b, fourBandWidths)
