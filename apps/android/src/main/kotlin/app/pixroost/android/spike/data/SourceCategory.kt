package app.pixroost.android.spike.data

import java.util.Locale

/** Where a photo came from, guessed from its folder or album: only the category leaves the phone. */
object SourceCategory {
    const val CAMERA = "Camera"
    const val SCREENSHOTS = "Screenshots"
    const val TELEGRAM = "Telegram"
    const val WHATSAPP = "WhatsApp"
    const val DOWNLOAD = "Download"
    const val OTHER = "other"

    val ALL = listOf(CAMERA, SCREENSHOTS, TELEGRAM, WHATSAPP, DOWNLOAD, OTHER)

    fun of(relativePath: String?, bucket: String?): String {
        val text = "${relativePath.orEmpty()} ${bucket.orEmpty()}".lowercase(Locale.ROOT)
        return when {
            "screenshot" in text || "скриншот" in text || "снимки экрана" in text -> SCREENSHOTS
            "telegram" in text -> TELEGRAM
            "whatsapp" in text -> WHATSAPP
            "camera" in text || "dcim" in text -> CAMERA
            "download" in text -> DOWNLOAD
            else -> OTHER
        }
    }
}
