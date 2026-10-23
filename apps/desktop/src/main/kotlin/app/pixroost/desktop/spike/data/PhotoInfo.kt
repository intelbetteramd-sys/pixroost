package app.pixroost.desktop.spike.data

/** What is known about an original photo, to see why some photos never match. No names, no paths. */
data class PhotoInfo(
    val width: Int,
    val height: Int,
    val extension: String,
    val orientation: Int?,
    val grey: GreyStats,
)
