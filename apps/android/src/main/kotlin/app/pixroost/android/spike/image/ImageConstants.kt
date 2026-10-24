package app.pixroost.android.spike.image

object ImageConstants {
    /** The size the system thumbnail is asked for; the app will hash previews of about this size. */
    const val THUMBNAIL_SIDE = 512

    /** The original is decoded down to a side of at least this many pixels, by a power-of-two sample size. */
    const val ORIGINAL_TARGET_SIDE = 1024

    /** Variant (e): the original decoded down to about this side instead of using a thumbnail. */
    const val DECODE_SIDE = 512

    const val JPEG_QUALITY = 80

    const val MIRROR_SCALE = -1f
    const val QUARTER_TURN = 90f
    const val HALF_TURN = 180f
}
