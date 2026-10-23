package app.pixroost.core.spike.phash

/** Whether [a] and [b] agree whole on at least one band; the bands have the given widths, lowest bits first. */
fun sharesBand(a: Long, b: Long, bandWidths: List<Int>): Boolean {
    var shift = 0
    for (width in bandWidths) {
        val mask = (1L shl width) - 1
        if ((a ushr shift) and mask == (b ushr shift) and mask) return true
        shift += width
    }
    return false
}
