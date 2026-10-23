package app.pixroost.desktop.spike.report

import java.util.Locale

private val russian = Locale.forLanguageTag("ru")

/** `String.format` with Russian separators (comma as the decimal mark). */
fun formatRu(pattern: String, vararg args: Any): String = String.format(russian, pattern, *args)
