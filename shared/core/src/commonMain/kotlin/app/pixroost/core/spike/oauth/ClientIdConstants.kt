package app.pixroost.core.spike.oauth

/**
 * Client IDs of the spike's test registrations. A client ID is public: it travels in the browser's address bar
 * anyway. Client secrets never go into the repository. An empty ID means the service is not registered yet.
 */
object ClientIdConstants {
    const val YANDEX = "637072533a4c4a02803747084b394fc9"
    const val DROPBOX = ""
    const val MICROSOFT = ""
    const val GOOGLE_ANDROID = "108643512167-lkoihif950hlfsu8ld9us5im146m0fbq.apps.googleusercontent.com"
    const val GOOGLE_DESKTOP = "108643512167-t3t3lubt35nai6j75o4uv19fhm5cva7k.apps.googleusercontent.com"
}
