package app.pixroost.core.spike.oauth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.io.IOException

/**
 * The whole Picker round: create a session, open Google's page, poll until the user taps "Done", download the
 * picked files and delete the session. [openUrl] gets the raw picker link: Android hands it to the Google Photos
 * app, the desktop adds `/autoclose` and opens the system browser.
 */
class GooglePickerFlow(
    private val picker: GooglePhotosPicker,
    private val scope: CoroutineScope,
    private val accessToken: () -> String?,
    private val openUrl: (String) -> Unit,
    private val log: (String) -> Unit,
    private val nowMillis: () -> Long,
    /** Keeps the open session's ID, so the picking survives the app being killed while Google Photos is open. */
    private val saveSession: (String?) -> Unit = {},
) {
    private val _status = MutableStateFlow("не запускался")
    val status: StateFlow<String> = _status.asStateFlow()

    fun start() = launch { token ->
        val session = picker.createSession(token)
        saveSession(session.id)
        log("Google Фото: сессия создана, открываю выбор")
        _status.value = "выберите фото в Google Фото, нажмите «Готово» и вернитесь в приложение"
        openUrl(session.pickerUri)
        waitAndDownload(token, session)
    }

    /** Picks up a session the previous run of the app opened. */
    fun resume(sessionId: String) = launch { token ->
        log("Google Фото: приложение перезапущено, продолжаю начатый выбор")
        _status.value = "продолжаю выбор после перезапуска приложения…"
        waitAndDownload(token, picker.getSession(token, sessionId))
    }

    private fun launch(block: suspend (String) -> Unit) {
        scope.launch {
            try {
                block(accessToken() ?: throw OAuthException("сначала войдите в Google"))
            } catch (error: IOException) {
                fail(error)
            } catch (error: OAuthException) {
                fail(error)
            }
        }
    }

    private suspend fun waitAndDownload(token: String, started: PickerSession) {
        var session = started
        val deadline = nowMillis() + OAuthSpikeConstants.SIGN_IN_TIMEOUT_MILLIS
        while (!session.isMediaItemsSet && nowMillis() < deadline) {
            delay(session.pollIntervalMillis)
            session = picker.getSession(token, session.id)
        }
        if (!session.isMediaItemsSet) throw OAuthException("выбор не завершён за 5 минут")
        val picked = picker.listPicked(token, session.id)
        _status.value = "выбрано ${picked.size}, скачиваю…"
        val startedAt = nowMillis()
        val bytes = picked.sumOf { picker.downloadSize(token, it) }
        picker.deleteSession(token, session.id)
        saveSession(null)
        val millis = nowMillis() - startedAt
        _status.value =
            "скачано ${picked.size} файлов, ${bytes / OAuthSpikeConstants.BYTES_IN_KILOBYTE} КБ за $millis мс"
        log("Google Фото: типы ${picked.groupingBy { it.mimeType }.eachCount()}")
    }

    private fun fail(error: Exception) {
        saveSession(null)
        _status.value = "ошибка: ${error.message}"
        log("Google Фото: ошибка ${error.message}")
    }
}
