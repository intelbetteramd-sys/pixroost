package app.pixroost.android.spike.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.pixroost.android.spike.data.GalleryAccess

@Composable
fun AccessRow(access: GalleryAccess, visible: Int, onRequestAccess: () -> Unit, modifier: Modifier = Modifier) {
    val label = when (access) {
        GalleryAccess.Full -> "Доступ: полный, видно фото: $visible"
        GalleryAccess.Partial -> "Доступ: частичный (Android 14), видно фото: $visible"
        GalleryAccess.None -> "Доступа к галерее нет"
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Button(onClick = onRequestAccess) { Text("Доступ") }
    }
}
