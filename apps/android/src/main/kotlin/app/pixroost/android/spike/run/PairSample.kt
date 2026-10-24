package app.pixroost.android.spike.run

/**
 * Thumbnail against original for one photo. [distance] is with the thumbnail as the system returns it,
 * [distanceRotated] with the thumbnail turned by the EXIF orientation once more. [thumbShape] and [originalShape]
 * are the sign of width minus height: positive landscape, negative portrait, 0 square.
 */
data class PairSample(
    val orientation: Int,
    val distance: Int,
    val distanceRotated: Int,
    val thumbBlank: Boolean,
    val originalBlank: Boolean,
    val thumbShape: Int,
    val originalShape: Int,
    val shape: PhotoShape,
    val variants: VariantSet,
)
