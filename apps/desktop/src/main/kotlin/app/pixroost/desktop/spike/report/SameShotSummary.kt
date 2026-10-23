package app.pixroost.desktop.spike.report

import app.pixroost.desktop.spike.data.PhotoSample
import app.pixroost.desktop.spike.data.Variant

/**
 * The spike's criterion: precision of "same shot" as if every photo had one worse copy, of each kind in turn, then
 * averaged over the kinds. Found copies are the true positives; the false ones are all pairs of frames of two
 * different photos within the threshold (see [SameShotCounts]). Two cases: a copy without EXIF (messengers),
 * checked by pHash alone, and a cloud copy that keeps the shot time, checked by the whole rule.
 */
fun sameShotLines(samples: List<PhotoSample>): List<String> {
    val counts = SameShotCounts()
    samples.forEach(counts::addCopies)
    for (i in samples.indices) {
        for (j in i + 1 until samples.size) counts.addPair(samples[i], samples[j])
    }
    val copies = samples.size * Variant.entries.size
    return listOf(
        precisionLine(
            "Копия без EXIF, только pHash ≤ ${ReportConstants.SAME_SHOT_DISTANCE}",
            counts.foundByHash,
            counts.falseByHash,
            copies,
        ),
        precisionLine("Копия с датой съёмки, всё правило", counts.foundByRule, counts.falseByRule, copies),
    )
}

private fun precisionLine(title: String, found: Int, falsePairs: Int, copies: Int): String {
    val kinds = Variant.entries.size
    val precision = if (found + falsePairs == 0) {
        "—"
    } else {
        formatRu("%.2f %%", ReportConstants.PERCENT * found / (found + falsePairs))
    }
    return "$title: найдено копий ${percent(found, copies)}, " +
        "ложных пар в среднем на вид копий ${formatRu("%.1f", falsePairs.toDouble() / kinds)}, точность $precision"
}
