package app.pixroost.desktop.spike.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.File
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO

/**
 * Reads the photos of a folder (with subfolders), hashes each one and its worse copies. Photos are only read; the
 * copies live in memory. A few photos at a time: a decoded 12 MP photo takes about 50 MB.
 */
class PhotoScanner {
    private val dispatcher = Dispatchers.Default.limitedParallelism(
        Runtime.getRuntime().availableProcessors().coerceAtMost(DataConstants.MAX_PARALLEL),
    )

    suspend fun scan(folder: File, onProgress: (done: Int, total: Int) -> Unit): ScanResult = coroutineScope {
        val startedAt = System.currentTimeMillis()
        val all = folder.walkTopDown().filter { it.isFile }.toList()
        val heic = all.count { it.extension.lowercase() in DataConstants.HEIC }
        val photos = all.filter { it.extension.lowercase() in DataConstants.DECODABLE }
            .sortedBy { it.path }
            .take(DataConstants.MAX_PHOTOS)
        val done = AtomicInteger()
        val samples = photos.map { file ->
            async(dispatcher) { sample(file).also { onProgress(done.incrementAndGet(), photos.size) } }
        }.awaitAll()
        ScanResult(
            samples = samples.filterNotNull(),
            found = photos.size,
            skippedHeic = heic,
            failed = samples.count { it == null },
            wallMillis = System.currentTimeMillis() - startedAt,
        )
    }

    private fun sample(file: File): PhotoSample? {
        val startedAt = System.nanoTime()
        val image = try {
            ImageIO.read(file)
        } catch (_: IOException) {
            null
        } ?: return null
        val decodedAt = System.nanoTime()
        val original = hashesOf(image)
        val hashedAt = System.nanoTime()
        return PhotoSample(
            original = original,
            shotTimeMillis = shotTimeMillis(file),
            variants = Variant.entries.associateWith { hashesOf(recode(image, it)) },
            decodeMillis = (decodedAt - startedAt) / DataConstants.NANOS_IN_MILLI,
            hashMillis = (hashedAt - decodedAt) / DataConstants.NANOS_IN_MILLI,
        )
    }
}
