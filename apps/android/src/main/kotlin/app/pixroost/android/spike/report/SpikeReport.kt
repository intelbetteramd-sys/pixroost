package app.pixroost.android.spike.report

import android.os.Build
import app.pixroost.android.spike.data.GalleryAccess
import app.pixroost.android.spike.run.RunSnapshot

/** The whole report: device, access, then whatever has been measured. No file names, numbers only. */
fun spikeReport(access: GalleryAccess, visible: Int, checkText: String?, snapshot: RunSnapshot?): String {
    val device = "${Build.MANUFACTURER} ${Build.MODEL}, Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    val head = "Отчёт S-06, телефон\nУстройство: $device\nДоступ к галерее: ${access.name}, видно фото: $visible"
    val thumbnailNote = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        "loadThumbnail нет (API < 29): превью из MediaStore.Images.Thumbnails"
    } else {
        "Превью: ContentResolver.loadThumbnail 512×512"
    }
    return listOf(
        head,
        thumbnailNote,
        checkText ?: "Проверка «500 фото» не запускалась",
        snapshot?.let { fullRunReport(it) } ?: "Проход по галерее не запускался",
    ).joinToString("\n\n")
}
