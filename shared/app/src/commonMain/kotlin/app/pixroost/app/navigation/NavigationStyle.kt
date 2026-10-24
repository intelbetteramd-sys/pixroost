package app.pixroost.app.navigation

/** How the main sections are shown. */
enum class NavigationStyle {
    /** Bar at the bottom of a phone screen. */
    BottomBar,

    /** Narrow rail at the side of a tablet. */
    Rail,

    /** Wide left pane with labels on a computer. */
    Pane,
    ;

    companion object {
        /** Picks the style for [platform] and a window [widthDp] wide. */
        fun forWindow(platform: AppPlatform, widthDp: Int): NavigationStyle = when (platform) {
            AppPlatform.Desktop -> Pane

            AppPlatform.Mobile ->
                if (widthDp < NavigationConstants.COMPACT_WIDTH_DP) BottomBar else Rail
        }
    }
}
