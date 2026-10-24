package app.pixroost.android.spike.run

import android.content.ContentResolver
import android.graphics.Bitmap
import app.pixroost.android.spike.data.GalleryPhoto
import app.pixroost.android.spike.image.ImageConstants
import app.pixroost.android.spike.image.PhotoHash
import app.pixroost.android.spike.image.decodeOriginal
import app.pixroost.android.spike.image.hashOf
import app.pixroost.android.spike.image.hashOfPixels
import app.pixroost.android.spike.image.loadLegacyThumbnail
import app.pixroost.android.spike.image.loadThumbnailAtAspect
import app.pixroost.android.spike.image.pixels
import app.pixroost.android.spike.image.software
import app.pixroost.core.spike.phash.PixelBox
import app.pixroost.core.spike.phash.centerCropBox
import app.pixroost.core.spike.phash.cropPixels
import app.pixroost.core.spike.phash.hammingDistance
import app.pixroost.core.spike.phash.trimBorders

/** Tries the candidate fixes (a)–(e) on one photo. [original] is the oriented decode at about 1024 px. */
fun measureVariants(
    resolver: ContentResolver,
    photo: GalleryPhoto,
    thumbnail: ThumbnailLoad,
    original: Bitmap,
    originalHash: PhotoHash,
): VariantSet {
    fun result(hash: PhotoHash?, reference: PhotoHash, nanos: Long, changed: Boolean = false) =
        VariantResult(hash?.let { hammingDistance(it.hash, reference.hash) }, nanos, changed)

    val (aspect, aspectNanos) = timed {
        loadThumbnailAtAspect(resolver, photo, original.width, original.height)?.software()?.let(::hashOf)
    }
    val (trimmed, trimmedNanos) = timed { trimmedHash(thumbnail) }
    val (cropped, croppedNanos) = timed { croppedHash(original, thumbnail.bitmap) }
    val (legacy, legacyNanos) = timed { loadLegacyThumbnail(resolver, photo)?.software()?.let(::hashOf) }
    val (decoded, decodedNanos) = timed {
        decodeOriginal(resolver, photo, ImageConstants.DECODE_SIDE)?.software()?.let(::hashOf)
    }
    return VariantSet(
        asIs = result(thumbnail.hash, originalHash, thumbnail.nanos),
        aspect = result(aspect, originalHash, aspectNanos),
        trimmed = result(trimmed.first, originalHash, trimmedNanos, trimmed.second),
        cropped = result(cropped, thumbnail.hash, croppedNanos),
        legacy = result(legacy, originalHash, legacyNanos),
        decoded = result(decoded, originalHash, decodedNanos),
    )
}

/** Hash of the thumbnail without its flat dark or light bars, and whether any bars were cut. */
private fun trimmedHash(thumbnail: ThumbnailLoad): Pair<PhotoHash, Boolean> {
    val bitmap = thumbnail.bitmap
    val pixels = bitmap.pixels()
    val box = trimBorders(pixels, bitmap.width, bitmap.height)
    val changed = box != PixelBox(0, 0, bitmap.width, bitmap.height)
    if (!changed) return thumbnail.hash to false
    return hashOfPixels(cropPixels(pixels, bitmap.width, box), box.width, box.height) to true
}

/** Hash of the original cut in the middle to the proportions of the thumbnail. */
private fun croppedHash(original: Bitmap, thumbnail: Bitmap): PhotoHash {
    val box = centerCropBox(original.width, original.height, thumbnail.width, thumbnail.height)
    return hashOfPixels(cropPixels(original.pixels(), original.width, box), box.width, box.height)
}
