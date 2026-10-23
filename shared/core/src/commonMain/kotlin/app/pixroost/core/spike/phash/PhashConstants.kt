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

    /** Grey values of the 32×32 image run from 0 to this. */
    const val GREY_MAX = 255f

    /**
     * A frame whose grey image has a standard deviation below this (on the 0–1 scale) is blank: black, white or
     * flat. Its pHash carries no picture, so blank frames take no part in matching.
     */
    const val BLANK_DEVIATION = 0.02

    /**
     * Two sizes have the same proportions when their aspect ratios differ by at most this share plus one pixel of
     * the smaller side (see [proportionsMatch]).
     */
    const val PROPORTION_TOLERANCE = 0.01

    const val MILLIS_IN_SECOND = 1000L
    const val SECONDS_IN_MINUTE = 60
    const val MINUTES_IN_HOUR = 60

    /** EXIF orientation values: 1 is the normal one, 2–8 mirror or rotate; 5–8 swap width and height. */
    const val ORIENTATION_FIRST_TRANSFORM = 2
    const val ORIENTATION_FIRST_SWAP = 5
    const val ORIENTATION_LAST = 8

    /** Confidence 95 %: with no hit in N trials the expected count is below 3 (the rule of three). */
    const val RULE_OF_THREE = 3.0
}
