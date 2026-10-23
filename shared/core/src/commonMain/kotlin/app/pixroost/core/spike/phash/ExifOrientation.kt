package app.pixroost.core.spike.phash

/** Whether the file records a non-trivial orientation, 2–8, that has to be applied to the pixels. */
fun needsOrientation(tag: Int?): Boolean =
    tag != null && tag in PhashConstants.ORIENTATION_FIRST_TRANSFORM..PhashConstants.ORIENTATION_LAST

/** Whether orientation [tag] turns the picture by a quarter, so width and height swap places (5–8). */
fun orientationSwapsSides(tag: Int?): Boolean =
    tag != null && tag in PhashConstants.ORIENTATION_FIRST_SWAP..PhashConstants.ORIENTATION_LAST
