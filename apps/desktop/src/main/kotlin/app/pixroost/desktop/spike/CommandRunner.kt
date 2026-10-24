package app.pixroost.desktop.spike

import java.io.IOException
import java.util.concurrent.TimeUnit

/** Runs external Windows tools (netsh, reg, powershell) and returns their output. */
object CommandRunner {
    fun run(vararg command: String): CommandResult = try {
        val process = ProcessBuilder(*command).redirectErrorStream(true).start()
        val output = process.inputStream.readBytes().toString(Charsets.UTF_8).trim()
        if (process.waitFor(SpikeConstants.COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            CommandResult(process.exitValue(), output)
        } else {
            process.destroyForcibly()
            CommandResult(-1, "timeout")
        }
    } catch (e: IOException) {
        CommandResult(-1, "failed to run ${command.first()}: ${e.message}")
    }

    fun powershell(script: String): CommandResult = run(
        "powershell.exe",
        "-NoProfile",
        "-NonInteractive",
        "-Command",
        SpikeConstants.POWERSHELL_UTF8_PREFIX + script,
    )
}
