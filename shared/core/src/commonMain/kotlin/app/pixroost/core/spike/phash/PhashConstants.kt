package app.pixroost.core.spike.phash

object PhashConstants {
    /** pHash works on a 32×32 grey image and keeps the 8×8 lowest frequencies of its DCT. */
    const val DCT_SIZE = 32
    const val HASH_SIZE = 8

    /** The index splits a 64-bit hash into this many bands; two hashes are candidates if one band matches. */
    const val LSH_BANDS = 4
    const val BITS_IN_HASH = 64

    /** The five-band scheme: 13 + 13 + 13 + 13 + 12 bits. */
    val FIVE_BAND_WIDTHS = listOf(13, 13, 13, 13, 12)
}
