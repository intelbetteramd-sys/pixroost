package app.pixroost.desktop.spike.report

/** "0,0061 %" for [part] of [whole]: four decimals, for shares far below one percent. */
fun percentPrecise(part: Int, whole: Int): String =
    if (whole == 0) "—" else formatRu("%.4f %%", part * ReportConstants.PERCENT / whole)
