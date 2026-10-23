package app.pixroost.desktop.spike.report

import app.pixroost.core.spike.phash.hammingDistance
import app.pixroost.desktop.spike.data.HashMethod
import app.pixroost.desktop.spike.data.PhotoSample
import app.pixroost.desktop.spike.data.Variant

/**
 * Photos whose halved copy lies farther than [ReportConstants.UNFOUND_DISTANCE] by either method. One anonymous
 * line each: number in the run, size, extension, EXIF orientation, brightness and spread of the 32×32 grey image,
 * and the distance to each copy by both methods ("по площади / по ступеням").
 */
fun unfoundPhotoLines(samples: List<PhotoSample>): List<String> {
    val limit = ReportConstants.UNFOUND_DISTANCE
    val lost = samples.withIndex().filter { (_, sample) ->
        val half = sample.variants.getValue(Variant.Half)
        HashMethod.entries.any { hammingDistance(sample.original[it], half[it]) > limit }
    }
    val order = Variant.entries.joinToString(", ") { it.label }
    return listOf(
        "Не найдены при ≤ $limit хотя бы одним способом: ${lost.size} из ${samples.size}",
        "Порядок копий в строках: $order; расстояния «по площади / по ступеням»",
    ) + lost.map { (index, sample) -> unfoundLine(index, sample) }
}

private fun unfoundLine(index: Int, sample: PhotoSample): String {
    val info = sample.info
    val distances = Variant.entries.joinToString(", ") { variant ->
        val copy = sample.variants.getValue(variant)
        val box = hammingDistance(sample.original[HashMethod.Box], copy[HashMethod.Box])
        val steps = hammingDistance(sample.original[HashMethod.Steps], copy[HashMethod.Steps])
        "$box/$steps"
    }
    return "№$index: ${info.width}×${info.height}, ${info.extension}, ориентация ${info.orientation ?: "нет"}, " +
        "яркость ${formatRu("%.2f", info.grey.mean)}, разброс ${formatRu("%.2f", info.grey.deviation)}; $distances"
}
