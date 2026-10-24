package app.pixroost.desktop.spike

/** Autostart for the .msi install: a value in HKCU Run. Inside MSIX the registry is virtualized, so not used there. */
object AutostartRegistry {
    fun isEnabled(): Boolean =
        CommandRunner.run("reg", "query", SpikeConstants.RUN_KEY, "/v", SpikeConstants.RUN_VALUE_NAME).exitCode == 0

    fun setEnabled(enabled: Boolean, executable: String) {
        if (enabled) {
            CommandRunner.run(
                "reg", "add", SpikeConstants.RUN_KEY, "/v", SpikeConstants.RUN_VALUE_NAME,
                "/t", "REG_SZ", "/d", "\"$executable\"", "/f",
            )
        } else {
            CommandRunner.run("reg", "delete", SpikeConstants.RUN_KEY, "/v", SpikeConstants.RUN_VALUE_NAME, "/f")
        }
    }
}
