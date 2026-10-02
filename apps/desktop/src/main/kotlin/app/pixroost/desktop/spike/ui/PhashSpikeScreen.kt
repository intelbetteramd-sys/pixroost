package app.pixroost.desktop.spike.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import app.pixroost.desktop.spike.report.copyToClipboard

/** Spike S-06: a folder of photos in, hash statistics out. */
@Composable
fun PhashSpikeScreen(controller: PhashSpikeController, modifier: Modifier = Modifier) {
    val state by controller.state.collectAsState()
    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(UiConstants.SCREEN_PADDING),
        verticalArrangement = Arrangement.spacedBy(UiConstants.SECTION_SPACING),
    ) {
        Text("Pixroost S-06: похожие фото", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = state.folder,
            onValueChange = controller::setFolder,
            label = { Text("Папка с фото") },
            singleLine = true,
            enabled = !state.isRunning,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(UiConstants.SECTION_SPACING),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(onClick = controller::start, enabled = !state.isRunning) { Text("Запустить") }
            OutlinedButton(onClick = { copyToClipboard(state.report) }, enabled = state.report.isNotEmpty()) {
                Text("Скопировать отчёт")
            }
            Text(state.status, style = MaterialTheme.typography.bodyMedium)
        }
        SelectionContainer {
            Text(state.report, style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace))
        }
    }
}
