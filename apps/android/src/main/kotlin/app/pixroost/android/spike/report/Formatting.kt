package app.pixroost.android.spike.report

import java.util.Locale

/** [part] of [whole] as "12 (3,4 %)"; the decimal comma is the Russian one. */
fun share(part: Int, whole: Int): String {
    val percent = if (whole == 0) 0.0 else part * ReportConstants.PERCENT / whole
    return "$part (${number(percent)} %)"
}

fun number(value: Double): String = String.format(Locale.forLanguageTag("ru"), "%.1f", value)

/** One line for a distance distribution. */
fun DistanceStats.line(): String =
    "n=$count; =0: ${share(zero, count)}; ≤2: ${share(upTo2, count)}; ≤4: ${share(upTo4, count)}; " +
        ">4: ${share(over4, count)}; медиана ${number(median)}, максимум $max"
