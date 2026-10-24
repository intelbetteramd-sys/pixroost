package app.pixroost.desktop.spike

/** What the spike window shows about the installation. */
data class InstallStatus(
    val installKind: String,
    val isMsix: Boolean,
    val executable: String,
    val dataDirectory: String,
    val dataNote: String,
    val firewallRules: String,
    val networkProfiles: String,
    val addresses: List<String>,
    val autostartEnabled: Boolean,
)
