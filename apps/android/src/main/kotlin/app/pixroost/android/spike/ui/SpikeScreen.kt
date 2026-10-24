package app.pixroost.android.spike.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import app.pixroost.android.spike.data.GalleryAccess
import app.pixroost.android.spike.ui.component.AccessRow
import app.pixroost.android.spike.ui.component.ProgressBlock
import app.pixroost.android.spike.ui.component.RunButtons
import app.pixroost.android.spike.ui.model.SpikeActions
import app.pixroost.android.spike.ui.model.SpikeUiState

/** Spike S-06 screen: access, the two runs, the report. The screen stays on while a run is going. */
@Composable
fun SpikeScreen(state: SpikeUiState, actions: SpikeActions, modifier: Modifier = Modifier) {
    val view = LocalView.current
    val running = state.running != null
    DisposableEffect(running) {
        view.keepScreenOn = running
        onDispose { view.keepScreenOn = false }
    }
    MaterialTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Спайк S-06: похожие фото на телефоне", style = MaterialTheme.typography.titleLarge)
            AccessRow(state.access, state.visible, actions.onRequestAccess)
            RunButtons(state.running, state.access != GalleryAccess.None, actions)
            if (running) ProgressBlock(state.done, state.total)
            state.previousRun?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            Text(state.message, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
