package app.pixroost.desktop.spike.data

/**
 * A worse copy of the same shot, as it appears in real life: the photo is scaled by [scale] or down to [maxSide]
 * and saved again as JPEG with [quality]. EXIF is lost on the way, as in messengers.
 */
enum class Variant(val label: String, val quality: Float, val scale: Double = 1.0, val maxSide: Int? = null) {
    Half("уменьшено вдвое", DataConstants.HALF_QUALITY, scale = DataConstants.HALF_SCALE),
    Jpeg60("JPEG, качество 60", DataConstants.LOW_QUALITY),
    Jpeg30("JPEG, качество 30", DataConstants.VERY_LOW_QUALITY),
    Telegram("как Telegram: 1280 px", DataConstants.TELEGRAM_QUALITY, maxSide = DataConstants.TELEGRAM_SIDE),
    WhatsApp("как WhatsApp: 1600 px", DataConstants.WHATSAPP_QUALITY, maxSide = DataConstants.WHATSAPP_SIDE),
    Preview("превью облака: 512 px", DataConstants.PREVIEW_QUALITY, maxSide = DataConstants.PREVIEW_SIDE),
}
