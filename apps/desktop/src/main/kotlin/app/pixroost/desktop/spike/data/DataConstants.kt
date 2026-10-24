package app.pixroost.desktop.spike.data

object DataConstants {
    /** What ImageIO decodes without extra libraries. HEIC is counted and skipped. */
    val DECODABLE = setOf("jpg", "jpeg", "png")
    val HEIC = setOf("heic", "heif")
    const val MAX_PHOTOS = 2000
    const val MAX_PARALLEL = 3
    const val NANOS_IN_MILLI = 1_000_000L

    /** EXIF tag 0x9011 `OffsetTimeOriginal`, a string such as "+03:00". */
    const val TAG_OFFSET_TIME_ORIGINAL = 0x9011

    const val HALF_SCALE = 0.5
    const val HALF_QUALITY = 0.9f
    const val LOW_QUALITY = 0.6f
    const val VERY_LOW_QUALITY = 0.3f
    const val TELEGRAM_SIDE = 1280
    const val TELEGRAM_QUALITY = 0.8f
    const val WHATSAPP_SIDE = 1600
    const val WHATSAPP_QUALITY = 0.7f
    const val PREVIEW_SIDE = 512
    const val PREVIEW_QUALITY = 0.75f
}
