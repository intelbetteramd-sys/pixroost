package app.pixroost.desktop.spike.report

import app.pixroost.desktop.spike.data.HashMethod
import app.pixroost.desktop.spike.data.ScanResult

/** Plain-text report to paste into the spike's issue. It holds no file names and no paths. */
fun buildPhashReport(result: ScanResult): String = buildString {
    val samples = result.samples
    val cores = Runtime.getRuntime().availableProcessors()
    appendLine("Pixroost S-06, ПК, второй прогон")
    appendLine(
        "Хеш: pHash, два способа сжатия до 32×32: «по ступеням» (деление пополам и билинейный шаг) и " +
            "«по площади» (среднее по площади). dHash убран из замеров: на уменьшенных копиях он заметно хуже",
    )
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
                "оба pHash ${samples.sumOf { it.hashMillis } / samples.size} мс",
        )
    }
    for (method in HashMethod.entries) {
        appendLine()
        appendLine("=== Сжатие ${method.label} ===")
        appendLine("Точность «тот же кадр» (как если бы у каждого фото была одна копия):")
        sameShotLines(samples, method).forEach { appendLine("  $it") }
        appendLine("Копии хуже качеством — доля найденных при пороге:")
        variantLines(samples, method).forEach { appendLine("  $it") }
        appendLine("Разные фото — ложные совпадения, рост с размером библиотеки, индекс:")
        distinctPairLines(samples, method).forEach { appendLine("  $it") }
    }
    appendLine()
    appendLine("=== Фото, чья копия вдвое меньше не нашлась ===")
    unfoundPhotoLines(samples).forEach { appendLine("  $it") }
}
