package app.pixroost.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import app.pixroost.android.spike.data.galleryPermissions
import app.pixroost.android.spike.run.SpikeModel
import app.pixroost.android.spike.ui.SpikeScreen
import app.pixroost.android.spike.ui.model.SpikeActions

class MainActivity : ComponentActivity() {
    private lateinit var model: SpikeModel

    private val permissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        model.refreshAccess()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        model = SpikeModel(applicationContext)
        model.refreshAccess()
        val actions = SpikeActions(
            onRequestAccess = { permissions.launch(galleryPermissions()) },
            onCheck = model::startCheck,
            onFull = model::startFull,
            onStop = model::stop,
            onReport = model::report,
        )
        setContent { SpikeScreen(model.state, actions) }
    }

    override fun onResume() {
        super.onResume()
        model.refreshAccess()
    }
}
