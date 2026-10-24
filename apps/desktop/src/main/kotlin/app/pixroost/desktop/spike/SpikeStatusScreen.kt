package app.pixroost.desktop.spike

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Status panel of the installer spike: how the app was installed and whether the phone can reach it. */
@Composable
fun SpikeStatusScreen(
    status: InstallStatus?,
    listenError: String?,
    requests: Int,
    onRefresh: () -> Unit,
    onAutostartChange: (Boolean) -> Unit,
    onOpenStartupSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Pixroost S-07: installer spike")
        if (status == null) {
            Text("Reading the status...")
        } else {
            StatusLines(status, listenError, requests)
            AutostartRow(status, onAutostartChange, onOpenStartupSettings)
        }
        Button(onClick = onRefresh) { Text("Обновить") }
    }
}

@Composable
private fun StatusLines(status: InstallStatus, listenError: String?, requests: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Установка: ${status.installKind}")
        Text("Исполняемый файл: ${status.executable}")
        Text("Данные: ${status.dataDirectory}")
        Text("Данные, проверка: ${status.dataNote}")
        Text("Правило брандмауэра:\n${status.firewallRules}")
        Text("Сетевой профиль:\n${status.networkProfiles}")
        Text(listenError ?: "Слушаем порт ${SpikeConstants.LISTEN_PORT}, запросов: $requests")
        Text("Проверить с телефона:")
        status.addresses.forEach { Text("http://$it:${SpikeConstants.LISTEN_PORT}/") }
    }
}

@Composable
private fun AutostartRow(
    status: InstallStatus,
    onAutostartChange: (Boolean) -> Unit,
    onOpenStartupSettings: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Запускать при входе в Windows")
        if (status.isMsix) {
            Button(onClick = onOpenStartupSettings) { Text("Открыть параметры автозапуска") }
        } else {
            Switch(checked = status.autostartEnabled, onCheckedChange = onAutostartChange)
        }
    }
}
