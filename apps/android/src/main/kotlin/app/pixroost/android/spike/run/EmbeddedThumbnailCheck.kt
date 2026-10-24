package app.pixroost.android.spike.run

import android.content.ContentResolver
import app.pixroost.android.spike.image.hashOf
import app.pixroost.android.spike.image.loadThumbnail
import app.pixroost.android.spike.image.readEmbeddedThumbnail
import app.pixroost.android.spike.image.software
import app.pixroost.core.spike.phash.hammingDistance

/**
 * One anonymous line about a photo: the size of the thumbnail inside its EXIF block, the size `loadThumbnail`
 * returned, and the distance between the two pictures. If the sizes or the pictures match, the system thumbnail
 * is the embedded one.
 */
fun embeddedThumbnailLine(resolver: ContentResolver, sample: PairSample): String {
    val photo = sample.shape.photo
    val embedded = readEmbeddedThumbnail(resolver, photo)?.software()
    val system = loadThumbnail(resolver, photo)?.software()
    val embeddedText = embedded?.let { "${it.width}×${it.height}" } ?: "нет"
    val systemText = system?.let { "${it.width}×${it.height}" } ?: "нет"
    val distance = if (embedded != null && system != null) {
        hammingDistance(hashOf(embedded).hash, hashOf(system).hash).toString()
    } else {
        "—"
    }
    return "EXIF-миниатюра $embeddedText; loadThumbnail $systemText; расстояние между ними $distance; " +
        "расстояние loadThumbnail-оригинал ${sample.distance}"
}
