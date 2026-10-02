package app.pixroost.desktop.spike.report

import java.util.Locale

/** "97,3 %" for [part] of [whole]. */
fun percent(part: Int, whole: Int): String = if (whole ==
    0
) {
    "—"
} else {
    String.format(Locale.forLanguageTag("ru"), "%.1f %%", part * ReportConstants.PERCENT / whole)
}
