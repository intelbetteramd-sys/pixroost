package app.pixroost.designsystem.catalog

/** Where the golden images live and how big the test window is (dp at density 1). */
internal object ScreenshotConstants {
    /** Relative to the module directory, where Gradle runs the tests. Committed to the repository. */
    const val GOLDEN_DIR = "src/desktopTest/screenshots"

    /** The root of the captured content; the image is cropped to it. */
    const val ROOT_TAG = "catalog"

    const val PHONE_WIDTH = 360
    const val DESKTOP_WIDTH = 480

    /** Taller than any catalog, also at 200% text; the image is cropped to the content. */
    const val WINDOW_HEIGHT = 8000

    const val FONT_SCALE_NORMAL = 1f
    const val FONT_SCALE_LARGEST = 2f
}
