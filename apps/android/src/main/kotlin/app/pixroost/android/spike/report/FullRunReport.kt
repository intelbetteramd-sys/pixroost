package app.pixroost.android.spike.report

import app.pixroost.android.spike.run.RunSnapshot

/** Text of the whole-gallery run, also for a run that was cut short. */
fun fullRunReport(snapshot: RunSnapshot): String {
    val state = if (snapshot.finished) "завершён" else "ОБОРВАН или остановлен"
    val battery = if (snapshot.batteryStart >= 0) {
        "Батарея: ${snapshot.batteryStart} % → ${snapshot.batteryNow} % " +
            "(${snapshot.batteryStart - snapshot.batteryNow} п.п.), " +
            if (snapshot.charging) "ЗАРЯДКА ПОДКЛЮЧЕНА, цифра не годится" else "без зарядки"
    } else {
        "Батарея: нет данных"
    }
    return listOf(
        "== Вся галерея, только по превью: $state",
        "Фото: ${snapshot.done} из ${snapshot.total}, не открылось ${snapshot.failed}, пустых кадров ${snapshot.blank}",
        "Время ${snapshot.elapsedMs / ReportConstants.MILLIS_IN_SECOND} с; ${number(snapshot.photosPerSecond)} фото/с; " +
            "${number(snapshot.photosPerMinute)} фото/мин",
        battery,
    ).joinToString("\n")
}
