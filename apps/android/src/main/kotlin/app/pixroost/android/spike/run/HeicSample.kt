package app.pixroost.android.spike.run

/** A HEIC photo against its own JPEG re-encode (quality 80), both hashed from a full oriented decode. */
data class HeicSample(val distance: Int, val blank: Boolean)
