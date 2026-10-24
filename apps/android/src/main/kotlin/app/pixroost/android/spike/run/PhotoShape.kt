package app.pixroost.android.spike.run

import app.pixroost.android.spike.data.GalleryPhoto

/**
 * What a mismatch report line needs: the sizes of the thumbnail as returned and of the original after
 * orientation, plus the photo itself (for the follow-up on its EXIF thumbnail; never printed).
 */
data class PhotoShape(
    val thumbWidth: Int,
    val thumbHeight: Int,
    val originalWidth: Int,
    val originalHeight: Int,
    val photo: GalleryPhoto,
)
