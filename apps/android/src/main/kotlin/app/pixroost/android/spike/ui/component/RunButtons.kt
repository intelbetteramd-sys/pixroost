package app.pixroost.android.spike.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.pixroost.android.spike.run.RunConstants
import app.pixroost.android.spike.ui.model.RunKind
import app.pixroost.android.spike.ui.model.SpikeActions

@Composable
fun RunButtons(running: RunKind?, canRun: Boolean, actions: SpikeActions, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = actions.onCheck, enabled = canRun && running == null, modifier = Modifier.fillMaxWidth()) {
            Text("Проверить ${RunConstants.CHECK_LIMIT} фото")
        }
        Button(onClick = actions.onFull, enabled = canRun && running == null, modifier = Modifier.fillMaxWidth()) {
            Text("Пройти всю галерею")
        }
        OutlinedButton(onClick = actions.onStop, enabled = running != null, modifier = Modifier.fillMaxWidth()) {
            Text("Остановить")
        }
        OutlinedButton(onClick = actions.onReport, modifier = Modifier.fillMaxWidth()) { Text("Отчёт") }
    }
}
