package app.pixroost.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

/**
 * Text styles by role. Sizes are `sp`, so they follow the system font scale; nothing is clamped
 * (docs/design/README.md, "Шрифты").
 *
 * Styles of headings, body text, list rows and bottom-bar labels hyphenate long words (`Hyphens.Auto`);
 * [caption] and [label] do not: a button label and a navigation rail label never break inside a word.
 * Plates (`SafetyNote`, `InfoNote`) use [body].
 */
@Immutable
class PixroostTypography(
    /** Unbounded 600, 28/36: titles of sections. */
    val title: TextStyle,
    /** Unbounded 600, 28/36 on a phone and 20/28 on the desktop: the big figures ("Освобождено 6,3 ГБ"). */
    val figure: TextStyle,
    /** Onest 600, 18/24 on a phone and 20/28 on the desktop: titles of blocks and of nested screens. */
    val heading: TextStyle,
    /** Onest 400, 15/22 and 14/20: text, plates. */
    val body: TextStyle,
    /** Onest 600 at the size of [body]: rows of lists, headings of cards. */
    val bodyStrong: TextStyle,
    /** Onest 600 at the size of [body], no hyphens: button labels. */
    val label: TextStyle,
    /** Onest 400, 13/18 and 12/16: captions, secondary lines. */
    val caption: TextStyle,
    /** Onest 400 at the size of [caption], with hyphens: labels of the bottom navigation bar. */
    val navigationLabel: TextStyle,
)
