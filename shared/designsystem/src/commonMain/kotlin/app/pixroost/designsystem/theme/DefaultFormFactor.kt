package app.pixroost.designsystem.theme

/** The form factor of the platform the code runs on; apps can still pass another one to [PixroostTheme]. */
internal expect fun defaultFormFactor(): PixroostFormFactor
