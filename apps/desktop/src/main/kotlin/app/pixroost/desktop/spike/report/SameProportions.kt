package app.pixroost.desktop.spike.report

import app.pixroost.core.spike.phash.proportionsMatch
import app.pixroost.desktop.spike.data.ImageHashes

/** Whether the two images have the same proportions, with the tolerance for 1 px of rounding of the smaller side. */
fun sameProportions(a: ImageHashes, b: ImageHashes): Boolean = proportionsMatch(a.width, a.height, b.width, b.height)
