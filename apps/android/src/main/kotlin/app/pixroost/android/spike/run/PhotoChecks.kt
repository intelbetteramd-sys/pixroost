package app.pixroost.android.spike.run

import android.content.ContentResolver
import android.media.ExifInterface
import app.pixroost.android.spike.data.GalleryPhoto
import app.pixroost.android.spike.image.ImageConstants
import app.pixroost.android.spike.image.decodeOriginal
import app.pixroost.android.spike.image.hashOf
import app.pixroost.android.spike.image.loadThumbnail
import app.pixroost.android.spike.image.orientedBy
import app.pixroost.android.spike.image.readOrientation
import app.pixroost.android.spike.image.readOrientedSize
import app.pixroost.android.spike.image.recodeToJpeg
import app.pixroost.android.spike.image.software
import app.pixroost.core.spike.phash.hammingDistance

/** Preview against original and the candidate fixes, null when either cannot be read. */
fun comparePhoto(resolver: ContentResolver, photo: GalleryPhoto): PairSample? = runCatching {
    val (loaded, loadNanos) = timed {
        loadThumbnail(resolver, photo)?.software()?.let { it to hashOf(it) }
    }
    val original = decodeOriginal(resolver, photo, ImageConstants.ORIGINAL_TARGET_SIDE)?.software()
    if (loaded == null || original == null) {
        null
    } else {
        val (thumbnail, thumbHash) = loaded
        val orientation = readOrientation(resolver, photo.uri)
        val originalHash = hashOf(original)
        val rotatedHash = if (orientation == ExifInterface.ORIENTATION_NORMAL) {
            thumbHash
        } else {
            hashOf(thumbnail.orientedBy(orientation))
        }
        val (fullWidth, fullHeight) = readOrientedSize(resolver, photo.uri, orientation)
            ?: (original.width to original.height)
        PairSample(
            orientation = orientation,
            distance = hammingDistance(thumbHash.hash, originalHash.hash),
            distanceRotated = hammingDistance(rotatedHash.hash, originalHash.hash),
            thumbBlank = thumbHash.isBlank,
            originalBlank = originalHash.isBlank,
            thumbShape = thumbHash.width.compareTo(thumbHash.height),
            originalShape = originalHash.width.compareTo(originalHash.height),
            shape = PhotoShape(thumbHash.width, thumbHash.height, fullWidth, fullHeight, photo),
            variants = measureVariants(
                resolver,
                photo,
                ThumbnailLoad(thumbnail, thumbHash, loadNanos),
                original,
                originalHash,
            ),
        )
    }
}.getOrNull()

/** HEIC original against its JPEG re-encode, null when it cannot be decoded. */
fun recodePhoto(resolver: ContentResolver, photo: GalleryPhoto): HeicSample? = runCatching {
    val original = decodeOriginal(resolver, photo, null)?.software()
    val recoded = original?.let { recodeToJpeg(it) }
    if (original == null || recoded == null) {
        null
    } else {
        val originalHash = hashOf(original)
        HeicSample(hammingDistance(originalHash.hash, hashOf(recoded).hash), originalHash.isBlank)
    }
}.getOrNull()

/** Hash of the system thumbnail alone, as the app will do it: blank or not, null when there is no thumbnail. */
fun hashThumbnailOnly(resolver: ContentResolver, photo: GalleryPhoto): Boolean? = runCatching {
    loadThumbnail(resolver, photo)?.software()?.let { hashOf(it).isBlank }
}.getOrNull()

/** Variant (e) alone: decode the original at about 512 px and hash it; true when it worked. */
fun hashDecodedOnly(resolver: ContentResolver, photo: GalleryPhoto): Boolean? = runCatching {
    decodeOriginal(resolver, photo, ImageConstants.DECODE_SIDE)?.software()?.let { hashOf(it).isBlank }
}.getOrNull()
