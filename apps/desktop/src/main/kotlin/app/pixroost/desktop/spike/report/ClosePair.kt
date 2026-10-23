package app.pixroost.desktop.spike.report

import app.pixroost.desktop.spike.data.PhotoSample

/** Two different photos whose pHash distance is small. */
class ClosePair(val first: PhotoSample, val second: PhotoSample, val distance: Int)
