package app.pixroost.desktop.spike.ui.model

data class PhashSpikeUiState(
    val folder: String = "",
    val status: String = "укажите папку с фото",
    val isRunning: Boolean = false,
    val report: String = "",

    /** File names of the close pairs: shown on the screen only, never copied with the report. */
    val pairNames: String = "",
)
