package app.pixroost.desktop.spike.report

import app.pixroost.desktop.spike.data.ScanResult

/** Plain-text report to paste into the spike's issue. It holds no file names and no paths. */
fun buildPhashReport(result: ScanResult): String = buildString {
    val samples = result.samples
    val cores = Runtime.getRuntime().availableProcessors()
    appendLine("Pixroost S-06, ПК")
    appendLine("ОС: ${System.getProperty("os.name")} ${System.getProperty("os.version")}, ядер: $cores")
    appendLine(
        "Фото: найдено ${result.found}, обработано ${samples.size}, не открылось ${result.failed}, " +
            "HEIC пропущено ${result.skippedHeic}, " +
            "с датой съёмки ${percent(samples.count { it.shotTimeMillis != null }, samples.size)}",
    )
    if (samples.isNotEmpty()) {
        appendLine(
            "Время: всего ${result.wallMillis / ReportConstants.MILLIS_IN_SECOND} с; на фото в среднем " +
                "декодирование ${samples.sumOf { it.decodeMillis } / samples.size} мс, " +
                "pHash и dHash ${samples.sumOf { it.hashMillis } / samples.size} мс",
        )
    }
    appendLine()
    appendLine("Точность «тот же кадр» (как если бы у каждого фото была одна копия):")
    sameShotLines(samples).forEach { appendLine("  $it") }
    appendLine()
    appendLine("Копии хуже качеством — доля найденных при пороге:")
    variantLines(samples).forEach { appendLine("  $it") }
    appendLine()
    appendLine("Разные фото — ложные совпадения:")
    distinctPairLines(samples).forEach { appendLine("  $it") }
}
