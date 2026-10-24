package app.pixroost.android.spike.ui.model

data class SpikeActions(
    val onRequestAccess: () -> Unit,
    val onCheck: () -> Unit,
    val onFull: () -> Unit,
    val onStop: () -> Unit,
    val onReport: () -> Unit,
)
