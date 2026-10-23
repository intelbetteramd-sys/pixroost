package app.pixroost.desktop.spike.report

import app.pixroost.desktop.spike.data.DataConstants
import app.pixroost.desktop.spike.data.ScanResult

/** Plain-text report to paste into the spike's issue. It holds no file names and no paths. */
fun buildPhashReport(result: ScanResult): String = buildString {
    val samples = result.samples
    val cores = Runtime.getRuntime().availableProcessors()
    appendLine("Pixroost S-06, ПК, четвёртый прогон")
    appendLine(
        "Хеш: pHash, сжатие до 32×32 по площади (среднее по площади). Пустые кадры в сопоставлении не участвуют",
    )
    appendLine("ОС: ${System.getProperty("os.name")} ${System.getProperty("os.version")}, ядер: $cores")
    appendLine(
        "Фото: найдено ${result.found} (читаю не больше ${DataConstants.MAX_PHOTOS}, взято ${result.taken}), " +
            "обработано ${samples.size}, не открылось ${result.failed}, HEIC пропущено ${result.skippedHeic}, " +
            "с датой съёмки ${percent(samples.count { it.shotTimeMillis != null }, samples.size)}",
    )
    appendLine("Номера не открывшихся файлов (по порядку чтения, с 1): ${failedNumbersText(result.failedNumbers)}")
    appendLine(
        "Ориентация EXIF не «нет» и не 1 (повёрнуты при чтении 2–8): " +
            percent(samples.count { it.info.orientation.let { tag -> tag != null && tag != 1 } }, samples.size),
    )
    if (samples.isNotEmpty()) {
        appendLine(
            "Время: всего ${result.wallMillis / ReportConstants.MILLIS_IN_SECOND} с; на фото в среднем " +
                "декодирование ${samples.sumOf { it.decodeMillis } / samples.size} мс, " +
                "pHash ${samples.sumOf { it.hashMillis } / samples.size} мс",
        )
    }
    appendLine()
    appendLine("=== Пустые кадры ===")
    blankFrameLines(samples).forEach { appendLine("  $it") }
    val kept = samples.filterNot { it.isBlank() }
    appendLine()
    appendLine("=== Остальные фото: ${kept.size} ===")
    appendLine("Точность «тот же кадр» (как если бы у каждого фото была одна копия):")
    sameShotLines(kept).forEach { appendLine("  $it") }
    appendLine("Копии хуже качеством — доля найденных при пороге:")
    variantLines(kept).forEach { appendLine("  $it") }
    appendLine("Разные фото — ложные совпадения, рост с размером библиотеки, индекс:")
    distinctPairLines(kept).forEach { appendLine("  $it") }
    appendLine()
    appendLine("=== Пары разных фото с близким pHash ===")
    closePairLines(samples).forEach { appendLine("  $it") }
}

private fun failedNumbersText(numbers: List<Int>): String =
    if (numbers.isEmpty()) "нет" else numbers.joinToString(", ") { "№$it" }
