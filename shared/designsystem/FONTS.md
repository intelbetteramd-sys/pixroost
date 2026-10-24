# Fonts

Bundled so the app never asks the network for fonts. Both families are under the SIL Open Font License 1.1;
the copyright lines and the license text are in the root `NOTICE`.

| File | Family and weight | Origin |
|---|---|---|
| `onest_regular.ttf` | Onest 400 | static instance of the upstream variable font |
| `onest_semibold.ttf` | Onest 600 | static instance of the upstream variable font |
| `unbounded_semibold.ttf` | Unbounded 600 | `fonts/ttf/Unbounded-SemiBold.ttf` of https://github.com/googlefonts/unbounded (commit f3ec432) |

Onest is published only as a variable font (`ofl/onest/Onest[wght].ttf` in https://github.com/google/fonts).
The static files were cut from it with fontTools 4.x, the same way Google Fonts makes its static downloads:

```
fonttools varLib.instancer "Onest[wght].ttf" wght=400 --update-name-table -o onest_regular.ttf
fonttools varLib.instancer "Onest[wght].ttf" wght=600 --update-name-table -o onest_semibold.ttf
```

Only the weights the design uses are committed (docs/design/README.md, "Шрифты"). Add a weight here only
together with a token that needs it.
