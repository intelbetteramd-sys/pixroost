package app.pixroost.desktop.spike.data

data class ScanResult(
    val samples: List<PhotoSample>,
    val found: Int,
    val skippedHeic: Int,
    val failed: Int,
    val wallMillis: Long,
)
