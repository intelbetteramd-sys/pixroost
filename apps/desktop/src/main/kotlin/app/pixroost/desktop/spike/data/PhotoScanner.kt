package app.pixroost.desktop.spike.data

import app.pixroost.core.spike.phash.resolveShotTimes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.File
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
        val decodable = all.filter { it.extension.lowercase() in DataConstants.DECODABLE }.sortedBy { it.path }
        val photos = decodable.take(DataConstants.MAX_PHOTOS)
        val done = AtomicInteger()
        val samples = photos.mapIndexed { index, file ->
            async(dispatcher) {
                sampleOrNull(index + 1, file.relativeTo(folder).path, file)
                    .also { onProgress(done.incrementAndGet(), photos.size) }
            }
        }.awaitAll()
        val read = samples.filterNotNull()
        val shotTimes = resolveShotTimes(read.map { it.wallTime })
        ScanResult(
            samples = read.mapIndexed { index, sample -> sample.copy(shotTimeMillis = shotTimes[index]) },
            found = decodable.size,
            taken = photos.size,
            skippedHeic = heic,
            failedNumbers = samples.indices.filter { samples[it] == null }.map { it + 1 },
            wallMillis = System.currentTimeMillis() - startedAt,
        )
    }

    /** A bad file of any kind (broken data, unsupported variant, out of memory) must never stop the whole scan. */
    private fun sampleOrNull(number: Int, name: String, file: File): PhotoSample? =
        runCatching { sample(number, name, file) }.getOrNull()

    private fun sample(number: Int, name: String, file: File): PhotoSample {
        val startedAt = System.nanoTime()
        val orientation = exifOrientation(file)
        val image = ImageIO.read(file)?.oriented(orientation) ?: error("not an image")
        val decodedAt = System.nanoTime()
        val grey = greyOf(image)
        val original = hashesOf(image, grey)
        val hashedAt = System.nanoTime()
        return PhotoSample(
            number = number,
            name = name,
            info = PhotoInfo(
                width = image.width,
                height = image.height,
                extension = file.extension.lowercase(),
                orientation = orientation,
                grey = greyStatsOf(grey),
            ),
            original = original,
            wallTime = wallTimeOf(file),
            shotTimeMillis = null,
            variants = Variant.entries.associateWith { hashesOf(recode(image, it)) },
            decodeMillis = (decodedAt - startedAt) / DataConstants.NANOS_IN_MILLI,
            hashMillis = (hashedAt - decodedAt) / DataConstants.NANOS_IN_MILLI,
        )
    }
}
