package app.pixroost.android.spike.ui.model

import app.pixroost.android.spike.data.GalleryAccess

data class SpikeUiState(
    val access: GalleryAccess = GalleryAccess.None,
    val visible: Int = 0,
    val running: RunKind? = null,
    val done: Int = 0,
    val total: Int = 0,
    val message: String = "",
    val previousRun: String? = null,
)
