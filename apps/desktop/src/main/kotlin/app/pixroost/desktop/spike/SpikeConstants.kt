package app.pixroost.desktop.spike

/** Constants of the S-07 installer spike. */
object SpikeConstants {
    const val LISTEN_PORT: Int = 47_077
    const val COMMAND_TIMEOUT_SECONDS: Long = 20
    const val FIREWALL_RULE_NAME: String = "Pixroost"
    const val RUN_KEY: String = "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run"
    const val RUN_VALUE_NAME: String = "Pixroost"
    const val DATA_DIR_NAME: String = "Pixroost"
    const val PROBE_FILE_NAME: String = "s07-probe.txt"
    const val STARTUP_SETTINGS_URI: String = "ms-settings:startupapps"
    const val MSIX_PATH_MARKER: String = "\\WindowsApps\\"
    const val TRAY_ICON_SIZE: Int = 32
    const val TRAY_ICON_MARGIN: Int = 4
    const val TRAY_ICON_COLOR: Int = 0xF2B84B
    const val WINDOW_WIDTH_DP: Int = 720
    const val WINDOW_HEIGHT_DP: Int = 640
    const val PAGE_TEXT: String = "Pixroost S-07: соединение есть"
    const val POWERSHELL_UTF8_PREFIX: String = "[Console]::OutputEncoding=[Text.Encoding]::UTF8; "
}
