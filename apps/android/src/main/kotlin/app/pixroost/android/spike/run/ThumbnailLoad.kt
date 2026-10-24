package app.pixroost.android.spike.run

import android.graphics.Bitmap
import app.pixroost.android.spike.image.PhotoHash

/** The thumbnail as the system returned it and its hash, loaded and hashed in [nanos]. */
class ThumbnailLoad(val bitmap: Bitmap, val hash: PhotoHash, val nanos: Long)
