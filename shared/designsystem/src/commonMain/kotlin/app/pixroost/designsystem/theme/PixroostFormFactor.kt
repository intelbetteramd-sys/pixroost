package app.pixroost.designsystem.theme

/**
 * The family of screens the interface is drawn for. The type scale and the corner radii differ between them
 * (docs/design/README.md, "Шрифты" and "Сетка и формы"); the colors do not.
 */
enum class PixroostFormFactor {
    /** Phones and tablets: Material 3 Expressive shapes, the 28/18/15/13 type scale. */
    Mobile,

    /** Windows, Linux and macOS: Fluent-like 4 px corners, the 28/20/14/12 type scale. */
    Desktop,
}
