package app.pixroost.core.spike.oauth

object YandexShareConstants {
    const val RESOURCES_URL = "https://cloud-api.yandex.net/v1/disk/resources"
    const val ROOT = "disk:/"
    const val PAGE_SIZE = 200
    const val MAX_PAGES_PER_FOLDER = 5
    const val MAX_FOLDERS = 2000
    const val MAX_DEPTH = 3
    const val MAX_DIRECT_FOLDERS = 10
    const val MAX_SCREEN_LINES = 200
    const val KIND_LISTING = "листинг папки"
    const val KIND_SHARED = "прямой: папка с share"
    const val KIND_SUBFOLDER = "прямой: вложенная папка"
    const val KIND_FILE = "прямой: вложенный файл"
    const val KIND_CONTROL = "прямой: папка без share"
    const val SHARE_KEY = "share"
    const val RIGHTS_KEY = "rights"

    /** Top-level keys of the documented Resource object; anything else the API returns is "unknown". */
    val DOCUMENTED_KEYS = setOf(
        "antivirus_status", "resource_id", "file", "size", "photoslice_time", "_embedded", "exif",
        "custom_properties", "media_type", "preview", "type", "mime_type", "revision", "public_url", "path",
        "md5", "sha256", "modified", "created", "name", "comment_ids", "public_key", "origin_path", "deleted",
    )
}
