package app.pixroost.android.spike.data

import android.content.Context
import app.pixroost.core.spike.oauth.CloudService
import app.pixroost.core.spike.oauth.Pkce

/**
 * Keeps the sign-in that waits for its redirect in the app's private preferences: phones like Xiaomi kill the app
 * while the browser is open, and without the verifier and the state the returning code cannot be exchanged.
 * Cleared as soon as the redirect arrives.
 */
class PendingSignInStore(context: Context) {
    private val preferences = context.getSharedPreferences(DataConstants.PENDING_PREFERENCES, Context.MODE_PRIVATE)

    fun save(signIn: PendingSignIn) {
        preferences.edit()
            .putString(DataConstants.PENDING_SERVICE_KEY, signIn.service.name)
            .putString(DataConstants.PENDING_STATE_KEY, signIn.state)
            .putString(DataConstants.PENDING_VERIFIER_KEY, signIn.pkce.verifier)
            .putString(DataConstants.PENDING_CHALLENGE_KEY, signIn.pkce.challenge)
            .putString(DataConstants.PENDING_REDIRECT_KEY, signIn.redirectUri)
            .apply()
    }

    fun take(): PendingSignIn? {
        val service = preferences.getString(DataConstants.PENDING_SERVICE_KEY, null)?.let(CloudService::valueOf)
        val signIn = service?.let {
            PendingSignIn(
                service = it,
                state = preferences.getString(DataConstants.PENDING_STATE_KEY, null).orEmpty(),
                pkce = Pkce(
                    preferences.getString(DataConstants.PENDING_VERIFIER_KEY, null).orEmpty(),
                    preferences.getString(DataConstants.PENDING_CHALLENGE_KEY, null).orEmpty(),
                ),
                redirectUri = preferences.getString(DataConstants.PENDING_REDIRECT_KEY, null).orEmpty(),
            )
        }
        preferences.edit().clear().apply()
        return signIn
    }
}
