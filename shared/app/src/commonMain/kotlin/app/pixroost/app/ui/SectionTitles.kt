package app.pixroost.app.ui

import app.pixroost.app.navigation.AppDestination

// Placeholder labels. They move to composeResources (RU and EN) together with the real screens.
internal fun AppDestination.title(): String = when (this) {
    AppDestination.Feed -> "Лента"
    AppDestination.Storages -> "Хранилища"
    AppDestination.Cleanup -> "Уборка"
    AppDestination.Devices -> "Устройства"
    AppDestination.Archive -> "Архив"
    AppDestination.Settings -> "Настройки"
}
