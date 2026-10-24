package app.pixroost.desktop.spike

/** Exit code and merged output of an external command. */
data class CommandResult(val exitCode: Int, val output: String)
