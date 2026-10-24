package app.pixroost.android.spike.run

/**
 * The candidate fixes for a thumbnail that is far from the original. [asIs] is the 512×512 `loadThumbnail` the
 * app would use today; its time covers loading and hashing. The others:
 * [aspect] thumbnail asked at the original's proportions, [trimmed] bars cut off the 512×512 thumbnail,
 * [cropped] the original cut to the thumbnail's proportions (distance to the thumbnail, not to the original),
 * [legacy] the old mini thumbnail, [decoded] the original decoded at about 512 px.
 */
data class VariantSet(
    val asIs: VariantResult,
    val aspect: VariantResult,
    val trimmed: VariantResult,
    val cropped: VariantResult,
    val legacy: VariantResult,
    val decoded: VariantResult,
)
