package app.pixroost.core.spike.phash

/** The pixels of [box] out of a [width]-wide image, row by row. */
fun cropPixels(argb: IntArray, width: Int, box: PixelBox): IntArray {
    val result = IntArray(box.width * box.height)
    for (y in 0 until box.height) {
        argb.copyInto(result, y * box.width, (box.top + y) * width + box.left, (box.top + y) * width + box.right)
    }
    return result
}
