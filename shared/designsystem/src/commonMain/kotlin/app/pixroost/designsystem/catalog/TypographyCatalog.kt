package app.pixroost.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.pixroost.designsystem.theme.PixroostSpacing
import app.pixroost.designsystem.theme.PixroostTokens

/** Every text style with a Cyrillic and a Latin sample, and the hyphenation of long words. */
@Composable
internal fun TypographyCatalog(modifier: Modifier = Modifier) {
    val typography = PixroostTokens.typography
    Column(
        modifier = modifier.padding(PixroostSpacing.space16),
        verticalArrangement = Arrangement.spacedBy(PixroostSpacing.space16),
    ) {
        TypeSample("title", typography.title, "Лента фото. Photo feed")
        TypeSample("figure", typography.figure, "Освобождено 6,3 ГБ")
        TypeSample("heading", typography.heading, "Где лежит это фото? Where it is")
        TypeSample("body", typography.body, "Съешь же ещё этих мягких французских булок, да выпей чаю. Pack my box.")
        TypeSample("bodyStrong", typography.bodyStrong, "Яндекс Диск · Dropbox · ПК «Дом»")
        TypeSample("label", typography.label, "Отправить на ПК")
        TypeSample("caption", typography.caption, "Сохранено дома · 12 июня 2026 · 4,2 МБ")
        TypeSample("navigationLabel", typography.navigationLabel, "Хранилища")
        HyphenationSample("hyphens: heading", typography.heading)
        HyphenationSample("hyphens: body", typography.body)
        HyphenationSample("hyphens: navigationLabel", typography.navigationLabel)
        HyphenationSample("no hyphens: caption", typography.caption)
    }
}
