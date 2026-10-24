package app.pixroost.designsystem.theme

/** Corner radii in dp; docs/design/README.md, "Сетка и формы". */
internal object ShapeConstants {
    /** Windows radius for everything the app draws, dialogs included; also the Android input field. */
    const val RADIUS_SMALLEST = 4

    /** Thumbnails and filter chips on a phone. */
    const val RADIUS_SMALL = 8

    /** Plates and badges on a phone. */
    const val RADIUS_PLATE = 16

    /** Cards and lists on a phone. */
    const val RADIUS_CARD = 20

    /** Sheets and dialogs on a phone. */
    const val RADIUS_SHEET = 28

    /** A fully rounded end: percent of the shorter side. */
    const val FULL_PERCENT = 50
}
