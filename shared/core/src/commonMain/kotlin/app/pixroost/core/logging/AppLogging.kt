package app.pixroost.core.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter

/**
 * Logging setup. Logs stay on the device: there are no remote or analytics writers, and there must not be
 * (privacy rule). Do not log tokens, file paths or file names at INFO and above.
 */
object AppLogging {
    /**
     * Installs [writers] (the system log by default) and drops everything below [minSeverity].
     * Call once at app start; debug builds pass [Severity.Debug], release builds [Severity.Info].
     */
    fun install(minSeverity: Severity, writers: List<LogWriter> = listOf(platformLogWriter())) {
        Logger.setLogWriters(writers)
        Logger.setMinSeverity(minSeverity)
        Logger.setTag(LoggingConstants.DEFAULT_TAG)
    }

    /**
     * Reads a severity from a setting such as a system property: "debug" or "info", case-insensitive.
     * Anything else, including `null`, gives [Severity.Info], so a release never logs more by accident.
     */
    fun severityFrom(value: String?): Severity =
        if (value.equals("debug", ignoreCase = true)) Severity.Debug else Severity.Info

    /** A logger with [tag], usually the class name. */
    fun logger(tag: String): Logger = Logger.withTag(tag)
}
