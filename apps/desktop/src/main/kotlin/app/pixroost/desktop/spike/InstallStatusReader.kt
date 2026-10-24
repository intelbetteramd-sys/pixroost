package app.pixroost.desktop.spike

import java.io.File
import java.io.IOException
import java.net.Inet4Address
import java.net.NetworkInterface

/** Collects the facts the spike is about: install kind, data location, firewall rule, network profile. */
object InstallStatusReader {
    fun read(): InstallStatus {
        val executable = ProcessHandle.current().info().command().orElse("unknown")
        val isMsix = executable.contains(SpikeConstants.MSIX_PATH_MARKER, ignoreCase = true)
        val localAppData = System.getenv("LOCALAPPDATA") ?: System.getProperty("user.home")
        val dataDir = File(localAppData, SpikeConstants.DATA_DIR_NAME)
        return InstallStatus(
            installKind = if (isMsix) "MSIX (app lives under WindowsApps)" else "classic install (.msi) or plain run",
            isMsix = isMsix,
            executable = executable,
            dataDirectory = dataDir.absolutePath,
            dataNote = probeDataDirectory(localAppData, dataDir),
            firewallRules = readFirewallRules(),
            networkProfiles = readNetworkProfiles(),
            addresses = localAddresses(),
            autostartEnabled = AutostartRegistry.isEnabled(),
        )
    }

    // MSIX redirects new files under AppData into Packages\<family>\LocalCache; look for the probe file there.
    private fun probeDataDirectory(localAppData: String, dataDir: File): String {
        val error = try {
            dataDir.mkdirs()
            File(dataDir, SpikeConstants.PROBE_FILE_NAME).writeText("s07")
            null
        } catch (e: IOException) {
            "write failed: ${e.message}"
        }
        val virtualized = File(localAppData, "Packages").listFiles().orEmpty()
            .map { File(it, "LocalCache\\Local\\${SpikeConstants.DATA_DIR_NAME}\\${SpikeConstants.PROBE_FILE_NAME}") }
            .firstOrNull { it.exists() }
        return when {
            error != null -> error
            virtualized != null -> "virtualized by MSIX: ${virtualized.absolutePath}"
            else -> "written to the real folder (not virtualized)"
        }
    }

    private fun readFirewallRules(): String {
        val script = "Get-NetFirewallApplicationFilter | Where-Object { \$_.Program -like '*Pixroost.exe' } | " +
            "ForEach-Object { \$r = \$_ | Get-NetFirewallRule; " +
            "\"\$(\$r.DisplayName) | profile=\$(\$r.Profile) | \$(\$r.Direction) | \$(\$r.Action) | " +
            "enabled=\$(\$r.Enabled) | \$(\$_.Program)\" }"
        val result = CommandRunner.powershell(script)
        return result.output.ifBlank { "no rule found for Pixroost.exe (exit ${result.exitCode})" }
    }

    private fun readNetworkProfiles(): String {
        val script = "Get-NetConnectionProfile | ForEach-Object { \"\$(\$_.Name): \$(\$_.NetworkCategory)\" }"
        return CommandRunner.powershell(script).output.ifBlank { "unknown" }
    }

    private fun localAddresses(): List<String> = try {
        NetworkInterface.getNetworkInterfaces().toList()
            .filter { it.isUp && !it.isLoopback }
            .flatMap { it.inetAddresses.toList() }
            .filterIsInstance<Inet4Address>()
            .map { it.hostAddress }
    } catch (e: IOException) {
        listOf("failed: ${e.message}")
    }
}
