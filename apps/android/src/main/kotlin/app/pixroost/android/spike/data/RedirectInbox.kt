package app.pixroost.android.spike.data

import android.net.Uri
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Hands the redirect from the browser to the screen. It keeps the last redirect until it is handled: when the app
 * was killed meanwhile, the redirect arrives before the screen exists to collect it.
 */
object RedirectInbox {
    private val redirects = MutableSharedFlow<Uri>(replay = 1)
    val received: SharedFlow<Uri> = redirects.asSharedFlow()

    fun deliver(uri: Uri) {
        redirects.tryEmit(uri)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun handled() = redirects.resetReplayCache()
}
