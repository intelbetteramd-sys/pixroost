package app.pixroost.desktop.spike.ui

import app.pixroost.desktop.spike.data.PhotoScanner
import app.pixroost.desktop.spike.report.buildPhashReport
import app.pixroost.desktop.spike.ui.model.PhashSpikeUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

/** State holder of the S-06 window: scan a folder, then show the report. */
class PhashSpikeController(private val scope: CoroutineScope) {
    private val scanner = PhotoScanner()
    private val _state = MutableStateFlow(PhashSpikeUiState())
    val state: StateFlow<PhashSpikeUiState> = _state.asStateFlow()

    fun setFolder(folder: String) = _state.update { it.copy(folder = folder) }

    fun start() {
        val folder = File(_state.value.folder.trim().trim('"'))
        if (!folder.isDirectory) {
            _state.update { it.copy(status = "папка не найдена") }
            return
        }
        _state.update { it.copy(isRunning = true, status = "ищу фото…", report = "") }
        scope.launch {
            val result = scanner.scan(folder) { done, total ->
                _state.update { it.copy(status = "обработано $done из $total") }
            }
            val report = buildPhashReport(result)
            _state.update {
                it.copy(isRunning = false, status = "готово: ${result.samples.size} фото", report = report)
            }
        }
    }

    fun close() = scope.cancel()
}
