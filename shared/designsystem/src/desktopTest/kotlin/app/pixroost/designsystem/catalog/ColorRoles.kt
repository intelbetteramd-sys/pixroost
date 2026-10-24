package app.pixroost.designsystem.catalog

import app.pixroost.designsystem.theme.PixroostColors

/** Every role of the palette in the order of docs/design/README.md. */
internal fun PixroostColors.roles(): List<ColorRole> = listOf(
    ColorRole("background", background),
    ColorRole("surface", surface),
    ColorRole("text", text),
    ColorRole("textSecondary", textSecondary),
    ColorRole("line", line),
    ColorRole("primary", primary),
    ColorRole("onPrimary", onPrimary),
    ColorRole("tonal", tonal),
    ColorRole("onTonal", onTonal),
    ColorRole("lamp", lamp),
    ColorRole("onLamp", onLamp),
    ColorRole("lampTint", lampTint),
    ColorRole("danger", danger),
    ColorRole("onDanger", onDanger),
    ColorRole("dangerTint", dangerTint),
    ColorRole("inverse", inverse),
    ColorRole("onInverse", onInverse),
    ColorRole("inverseAccent", inverseAccent),
    ColorRole("outline", outline),
    ColorRole("focus", focus),
    ColorRole("scrim", scrim),
    ColorRole("shadowKey", shadowKey),
    ColorRole("shadowAmbient", shadowAmbient),
)
