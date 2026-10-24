package app.pixroost.app.navigation

/** How the main sections are shown. */
enum class NavigationStyle {
    /** Bar at the bottom of a phone screen. */
    BottomBar,

    /** Narrow rail at the side of a tablet. */
    Rail,

    /** Wide left pane with labels on a computer. */
    Pane,
}

/** Picks the [NavigationStyle] for [platform] and a window [widthDp] wide. */
fun navigationStyleFor(platform: AppPlatform, widthDp: Int): NavigationStyle = when (platform) {
    AppPlatform.Desktop -> NavigationStyle.Pane
    AppPlatform.Mobile ->
        if (widthDp < NavigationConstants.COMPACT_WIDTH_DP) NavigationStyle.BottomBar else NavigationStyle.Rail
}
