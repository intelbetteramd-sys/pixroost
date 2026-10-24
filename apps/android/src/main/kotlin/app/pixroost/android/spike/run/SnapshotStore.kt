package app.pixroost.android.spike.run

import android.content.Context

/** Keeps the last run snapshot and the text of the last preview check across app restarts. */
class SnapshotStore(context: Context) {
    private val prefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun save(snapshot: RunSnapshot) {
        prefs.edit()
            .putInt(TOTAL, snapshot.total)
            .putInt(DONE, snapshot.done)
            .putInt(FAILED, snapshot.failed)
            .putInt(BLANK, snapshot.blank)
            .putLong(ELAPSED, snapshot.elapsedMs)
            .putInt(BATTERY_START, snapshot.batteryStart)
            .putInt(BATTERY_NOW, snapshot.batteryNow)
            .putBoolean(CHARGING, snapshot.charging)
            .putBoolean(FINISHED, snapshot.finished)
            .putBoolean(PRESENT, true)
            .apply()
    }

    fun load(): RunSnapshot? = if (!prefs.getBoolean(PRESENT, false)) {
        null
    } else {
        RunSnapshot(
            total = prefs.getInt(TOTAL, 0),
            done = prefs.getInt(DONE, 0),
            failed = prefs.getInt(FAILED, 0),
            blank = prefs.getInt(BLANK, 0),
            elapsedMs = prefs.getLong(ELAPSED, 0),
            batteryStart = prefs.getInt(BATTERY_START, 0),
            batteryNow = prefs.getInt(BATTERY_NOW, 0),
            charging = prefs.getBoolean(CHARGING, false),
            finished = prefs.getBoolean(FINISHED, false),
        )
    }

    fun saveCheckText(text: String) {
        prefs.edit().putString(CHECK_TEXT, text).apply()
    }

    fun loadCheckText(): String? = prefs.getString(CHECK_TEXT, null)

    private companion object {
        const val FILE = "s06"
        const val PRESENT = "present"
        const val TOTAL = "total"
        const val DONE = "done"
        const val FAILED = "failed"
        const val BLANK = "blank"
        const val ELAPSED = "elapsed"
        const val BATTERY_START = "battery_start"
        const val BATTERY_NOW = "battery_now"
        const val CHARGING = "charging"
        const val FINISHED = "finished"
        const val CHECK_TEXT = "check_text"
    }
}
