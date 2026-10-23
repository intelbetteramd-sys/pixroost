package app.pixroost.core.spike.phash

/**
 * The same check with 5 bands of 13, 13, 13, 13 and 12 bits. By the pigeonhole rule at most 4 differing bits cannot
 * spoil all five bands, so every pair at distance ≤ 4 is found; the price is more candidates among random pairs.
 */
fun sharesFiveLshBands(a: Long, b: Long): Boolean = sharesBand(a, b, PhashConstants.FIVE_BAND_WIDTHS)
