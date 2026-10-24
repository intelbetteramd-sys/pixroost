package app.pixroost.android.spike.report

import app.pixroost.android.spike.run.RunSnapshot

/** Text of the whole-gallery run, also for a run that was cut short. */
fun fullRunReport(snapshot: RunSnapshot): String {
    val state = if (snapshot.finished) "завершён" else "ОБОРВАН или остановлен"
    val battery = if (snapshot.batteryStart >= 0) {
        val used = snapshot.batteryStart - snapshot.batteryNow
        val charger = if (snapshot.charging) "ЗАРЯДКА ПОДКЛЮЧЕНА, цифра не годится" else "без зарядки"
        "Батарея: ${snapshot.batteryStart} % → ${snapshot.batteryNow} % ($used п.п.), $charger"
    } else {
        "Батарея: нет данных"
    }
    val counts = "Фото: ${snapshot.done} из ${snapshot.total}, не открылось ${snapshot.failed}, " +
        "пустых кадров ${snapshot.blank}"
    val seconds = snapshot.elapsedMs / ReportConstants.MILLIS_IN_SECOND
    return listOf(
        "== Вся галерея, только по превью: $state",
        counts,
        "Время $seconds с; ${number(snapshot.photosPerSecond)} фото/с; ${number(snapshot.photosPerMinute)} фото/мин",
        battery,
    ).joinToString("\n")
}
