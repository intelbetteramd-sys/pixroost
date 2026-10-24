package app.pixroost.core.spike.oauth

/** What the flat list showed for the files the walk found inside shared folders. */
data class YandexFlatResult(
    val kind: String,
    val listed: Int,
    val sharedFiles: Int,
    val found: Int,
    val withShare: Int,
)
