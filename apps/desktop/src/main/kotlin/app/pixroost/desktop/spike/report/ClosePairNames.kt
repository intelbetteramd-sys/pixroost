package app.pixroost.desktop.spike.report

import app.pixroost.desktop.spike.data.PhotoSample

/**
 * The same close pairs as in the report, with file names, to look at the pictures without a script. Shown on the
 * screen below the report only: it is never part of the copied text.
 */
fun closePairNameLines(all: List<PhotoSample>): List<String> = closePairs(all).map { pair ->
    "№${pair.first.number} ${pair.first.name} — №${pair.second.number} ${pair.second.name} " +
        "(расстояние ${pair.distance})"
}
