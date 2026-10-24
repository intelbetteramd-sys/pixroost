package app.pixroost.android.spike.image

import app.pixroost.core.spike.phash.isBlankFrame

/** pHash of one picture with the size it was taken from and whether it is a blank frame. */
data class PhotoHash(val hash: Long, val width: Int, val height: Int, val deviation: Double) {
    val isBlank: Boolean
        get() = isBlankFrame(deviation)
}
