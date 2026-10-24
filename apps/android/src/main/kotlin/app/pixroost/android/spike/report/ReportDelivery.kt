package app.pixroost.android.spike.report

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log

/** Puts the report on the clipboard and into Logcat (one line per entry, Logcat cuts long messages). */
fun deliverReport(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("Pixroost S-06", text))
    text.lines().forEach { Log.i(ReportConstants.LOG_TAG, it) }
}
