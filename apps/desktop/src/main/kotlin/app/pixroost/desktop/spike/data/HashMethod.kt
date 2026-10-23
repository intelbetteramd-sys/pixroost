package app.pixroost.desktop.spike.data

/** How the photo is squeezed to the 32×32 grey image before the DCT. */
enum class HashMethod(val label: String) {
    Steps("по ступеням"),
    Box("по площади"),
}
