package app.pixroost.desktop.spike.ui.model

import app.pixroost.core.spike.oauth.CloudService

/** One cloud on the screen: whether the app has a token, what the token allows, what the service answered. */
data class ServiceUiState(
    val service: CloudService,
    val status: String = "не подключено",
    val account: String? = null,
    val tokenInfo: String? = null,
    /** Which content hashes the first page of files came with. */
    val media: String? = null,
    /** Counts and anonymised lines of the shared-folders probe; they go to the report. */
    val shareSummary: List<String> = emptyList(),
    /** Folder paths with their `share` value; shown on the screen only, never copied. */
    val shareFolders: List<String> = emptyList(),
    val isBusy: Boolean = false,
)
