package app.pixroost.desktop.spike.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.pixroost.desktop.spike.ui.UiConstants
import app.pixroost.desktop.spike.ui.model.ServiceUiState

/** One cloud: sign in through the browser, refresh the token, list a few files, sign out. */
@Composable
fun ServiceCard(
    state: ServiceUiState,
    onSignIn: () -> Unit,
    onRefresh: () -> Unit,
    onSignOut: () -> Unit,
    onSample: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val style = MaterialTheme.typography.bodyMedium
    SectionCard(state.service.label, modifier) {
        Text(state.status, style = style)
        state.account?.let { Text(it, style = style) }
        state.tokenInfo?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
        state.media?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
        // On a phone four buttons do not fit in one row; signed in, "Войти" gives way to the other actions.
        FlowRow(horizontalArrangement = Arrangement.spacedBy(UiConstants.ROW_SPACING)) {
            if (state.tokenInfo == null) {
                Button(onClick = onSignIn, enabled = !state.isBusy) { Text("Войти") }
            } else {
                OutlinedButton(onClick = onRefresh, enabled = !state.isBusy) { Text("Обновить токен") }
                OutlinedButton(onClick = onSample, enabled = !state.isBusy) { Text("Список фото") }
                OutlinedButton(onClick = onSignOut, enabled = !state.isBusy) { Text("Выйти") }
            }
        }
    }
}
