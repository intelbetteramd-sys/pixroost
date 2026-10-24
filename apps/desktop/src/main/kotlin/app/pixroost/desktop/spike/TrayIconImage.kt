package app.pixroost.desktop.spike

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.awt.Color
import java.awt.image.BufferedImage

/** A placeholder tray icon (a lit square); the real icon comes with step 2.2. */
fun createTrayIcon(): BitmapPainter {
    val size = SpikeConstants.TRAY_ICON_SIZE
    val margin = SpikeConstants.TRAY_ICON_MARGIN
    val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val graphics = image.createGraphics()
    graphics.color = Color(SpikeConstants.TRAY_ICON_COLOR)
    graphics.fillRoundRect(margin, margin, size - margin * 2, size - margin * 2, margin * 2, margin * 2)
    graphics.dispose()
    return BitmapPainter(image.toComposeImageBitmap())
}
