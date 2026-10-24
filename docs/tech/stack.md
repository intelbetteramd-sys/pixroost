# Технологический стек

Версии — актуальные на сентябрь 2026. Точные номера фиксируются в `gradle/libs.versions.toml`,
обновления идут через Dependabot.

## Основа

| Что | Выбор | Почему |
|---|---|---|
| Язык | **Kotlin 2.4.x** | один язык для всех платформ, сервера и сборки; поддержка Xcode 26 |
| Мультиплатформа | **Kotlin Multiplatform** | общий код для Android, iOS, JVM desktop ([ADR 0001](../architecture/adr/0001-kotlin-multiplatform-compose.md)) |
| UI | **Compose Multiplatform 1.12+** | один UI-код; стабилен на iOS с 1.8, общий `@Preview` и Hot Reload с 1.10, нативный ввод текста на iOS с 1.11 |
| Сборка | Gradle 9 + Kotlin DSL, version catalog, convention-плагины в `build-logic` | стандарт для KMP |
| Среда разработки | Android Studio или IntelliJ IDEA + плагин Kotlin Multiplatform, Xcode 26 | запуск и отладка iOS из IDE на Mac |

## Библиотеки

| Задача | Выбор | Альтернатива |
|---|---|---|
| Асинхронность | kotlinx.coroutines 1.11 + Flow; диспетчеры берём через `DispatcherProvider`, в тестах — один тестовый | — |
| Сериализация | kotlinx.serialization (JSON) | — |
| Сеть (клиент) | **Ktor Client 3** (OkHttp на Android и desktop, Darwin на iOS) | — |
| Сеть (сервер приёма на ПК, relay) | **Ktor Server 3**, движок Netty (проверено в S-03) | — |
| Локальная БД | **Room 3** (KSP, `BundledSQLiteDriver`) + Paging 3 ([ADR 0010](../architecture/adr/0010-room3.md)) | SQLDelight 2 |
| Настройки | DataStore (KMP) | multiplatform-settings |
| DI | **Koin 4** | Metro, kotlin-inject |
| Навигация | Navigation 3 (поддерживается в Compose Multiplatform с 1.10) | Decompose |
| ViewModel / жизненный цикл | androidx.lifecycle (KMP) | — |
| Загрузка изображений | **Coil 3** со своими загрузчиками для каждого источника | — |
| Видео | Media3 ExoPlayer (Android), AVPlayer (iOS), VLCJ (desktop) — ⚠️ спайк | — |
| Криптография | cryptography-kotlin (JDK, Apple, OpenSSL-провайдеры) | libsodium-kmp |
| TLS-сертификат ПК | самоподписанный ECDSA P-256, BouncyCastle (`bcpkix`) на JVM: в JDK нет API для создания сертификатов; телефон пиннит отпечаток (спайк S-03) | — |
| Работа с файлами | kotlinx-io | Okio |
| Логи | Kermit 2.2: только системный журнал, без удалённых писателей (правило приватности); минимальная важность задаётся при старте | — |
| QR: генерация | qrcode-kotlin | ZXing |
| QR: сканирование | CameraX + ZXing (Android), AVFoundation / VisionKit (iOS) | ML Kit — не берём: проприетарный, мешает F-Droid |
| Поиск устройств | mDNS: NsdManager (Android), Network.framework (iOS), JmDNS (desktop). Для развёрнутого подключения без правила брандмауэра — UDP-рассылка «кто тут?» на широковещательные адреса (S-04) | системная служба mDNS Windows (DNS-SD API) не понадобилась |
| OAuth | системный браузер + PKCE: Custom Tabs (`androidx.browser`, Android), `ASWebAuthenticationSession` (iOS), loopback-редирект на `HttpServer` из JDK (desktop). Общий код входа — на Ktor Client в `commonMain`, около 300 строк (спайк S-05) | AppAuth |
| Фоновая работа | WorkManager (Android), BGTaskScheduler + background URLSession (iOS) | — |
| Безопасное хранение | Keychain, Android Keystore (AES-GCM), DPAPI через JNA `Crypt32Util` (Windows), Secret Service (Linux). Не java-keyring: не обновляется с 2023 года, а Credential Manager держит не больше 2,5 КБ на запись | — |
| Perceptual hash | своя реализация pHash (DCT 32×32, 64 бита) на общем Kotlin; dHash отвергнут: на копиях другого размера заметно хуже ([S-06](../architecture/data-model.md#как-считаем-phash)) | — |

## Основа `shared/core`

Шаг 3.3 плана. Что лежит в модуле и почему так:

- **Ошибки и результат.** Свой `AppResult<T>` (`Success` / `Failure`) с типизированными `AppError`: сеть, вход,
  доступ, «не найдено», хранилище, неожиданная. Вместо `kotlin.Result`: ошибка типизирована и проверяется в `when`
  на полноту, UI по виду ошибки показывает разный текст. `appCatching { … }` превращает исключение в `Failure`, но
  `CancellationException` пробрасывает: иначе ломается структурная конкурентность.
- **Время.** Отдельный слой не нужен: в Kotlin 2.4 стабильны `kotlin.time.Clock` и `Instant`. Код принимает `Clock`
  параметром, в приложении — `Clock.System`, в тестах — фейк. `kotlinx-datetime` добавим, когда понадобятся часовые пояса
  и календарные даты.
- **Диспетчеры.** `DispatcherProvider` (`main`, `io`, `default`) и `DefaultDispatcherProvider`; прямой доступ к
  `Dispatchers.*` в прикладном коде не используем.
- **Логи.** `AppLogging.install(minSeverity)` один раз при старте приложения; в журнал не пишем токены, пути и имена
  файлов на уровнях INFO и выше.

## Итоги спайков Фазы 1

Что выбрали по результатам спайков S-02 – S-07 ([план, Фаза 1](../development-plan.md#фаза-1--каркас-и-спайки)).
Измерения, таблицы и ссылки на отчёты — в документах, на которые указывают строки. Новых ADR спайки не потребовали:
[ADR 0005](../architecture/adr/0005-lan-transport.md) подтверждён.

| Спайк | Что выбрали | Подробности |
|---|---|---|
| S-02, галерея Android | индекс MediaStore в нашей БД (полный проход при запуске и по сигналу, изменения по `GENERATION_MODIFIED` на Android 11+, удалённые сравнением ID), лента из БД через Paging 3; превью — `ContentResolver.loadThumbnail` через свой загрузчик Coil 3, оригиналы в сетке не декодируем. Постраничное чтение MediaStore не нужно | [отчёт #22](https://github.com/intelbetteramd-sys/pixroost/issues/22) |
| S-03, передача на ПК | Ktor Server (Netty) с самоподписанным ECDSA P-256 на ПК, Ktor Client (OkHttp) с пиннингом отпечатка на Android, докачка в духе tus с идентификатором загрузки SHA-256 | [ADR 0005](../architecture/adr/0005-lan-transport.md#итоги-спайка), [передача](../architecture/devices-and-transfer.md#что-показал-спайк-s-03) |
| S-04, поиск ПК | основной путь — прямой (mDNS и HTTPS-сервер на ПК, нужно правило брандмауэра), запасной — развёрнутый (рассылка и соединение от ПК); с VPN не работает ни один | [путь соединения](../architecture/devices-and-transfer.md#путь-соединения) |
| S-05, вход в облака | системный браузер и PKCE на своём коде поверх Ktor Client в `commonMain`, без AppAuth и без секретов в коде (кроме секрета Google Desktop-клиента, который Google секретом не считает; сборка подставит его из секретов CI, в репозитории его нет; в CI он пока не подключён). Android: Custom Tabs и своя схема; ПК: loopback-редирект. Яндекс Диск, Dropbox, Google Photos Picker, OneDrive проверены на Android 12 и Windows 11. Токены — Android Keystore (AES-GCM) и DPAPI. Начатый вход и сессию Picker храним и продолжаем после перезапуска: MIUI выгружает приложение, пока открыт браузер | [облака](../product/sources.md), [правило уборки для общих папок Яндекса](../architecture/adr/0011-cleanup-safety-rule.md) |
| S-06, похожие фото | pHash (DCT 32×32, 64 бита, сжатие по площади), только по изображению всего кадра: пропорции превью сверяем с оригиналом и при расхождении больше 1 % декодируем оригинал до ~512 px | [data-model](../architecture/data-model.md#как-считаем-phash) |
| S-07, установщик Windows | Microsoft Store (MSIX) — основной канал; `.exe` на Inno Setup — сайт и GitHub Releases; `.msi` только для корпоративной раздачи. Правило брандмауэра для всех профилей сети ставит установщик; для релизного установщика — мьютекс приложения вместо `taskkill` из спайка ([порядок остановки](../release/distribution.md#windows)) | [distribution](../release/distribution.md#windows) |

Не проверено и вынесено в следующие шаги:

- iPhone: фоновая `URLSession` с пиннингом, Bonjour и запрос доступа к локальной сети (S-03; ждёт Mac, [#37](https://github.com/intelbetteramd-sys/pixroost/issues/37)).
- Скорость передачи: в сети спайка 4–5 МБ/с, оба устройства на Wi-Fi; цель ≥ 20 МБ/с не проверена.
- Частичный доступ к галерее Android 14+ и превью на Android 8–9 (S-02; шаги 3.7 и 3.10).
- pHash: ориентация EXIF на телефонах, где она записана, копии HEIC → JPEG, iPhone (S-06).
- Установка `.msi`; повторная проверка обновления и удаления `.exe` при запущенном приложении в сборке спайка (`taskkill`; пункт 3.4 в #83). Мьютекс нужен только релизному установщику. Решение
  это не меняет.

## Качество

| Что | Инструмент |
|---|---|
| Тесты | kotlin.test, Turbine (Flow), kotlinx-coroutines-test |
| UI-тесты | Compose UI testing (multiplatform) |
| Скриншот-тесты компонентов | Roborazzi на desktop (JVM): Compose Desktop рисует через Skia, как и Android с iOS; эталоны записаны на Linux CI |
| E2E на мобильных | Maestro |
| Стиль и анализ | ktlint (через Spotless), detekt |
| Покрытие | Kover |
| Безопасность | CodeQL, Dependabot, Gradle dependency verification |

## Desktop

| Что | Выбор |
|---|---|
| UI | Compose for Desktop (JVM 25, встроенная JRE) |
| Упаковка | Compose `createDistributable` (образ приложения) + jpackage: `.dmg` (macOS), `.deb` (Linux). Windows: `.exe` на Inno Setup (сайт), MSIX собираем сами (`makeappx` из Windows SDK по своему `AppxManifest.xml`); `.msi` через jpackage со своим `main.wxs` — только для корпоративной раздачи ([S-07](https://github.com/intelbetteramd-sys/pixroost/issues/27)) |
| Подпись macOS | Developer ID + нотаризация (`notarytool`) |
| Microsoft Store | MSIX (в Compose такого формата нет, сборка скриптом), подпись выполняет Microsoft |
| Трей / строка меню | Compose `Tray` на Windows и Linux; на macOS — `NSStatusItem` с панелью на SwiftUI |
| Автозапуск | LaunchAgent (macOS); Windows: ключ `HKCU\…\Run`, который приложение пишет само при первом запуске, для `.exe`; `windows.startupTask` для MSIX (переключатель реестра там не работает) |
| Горячая перезагрузка UI | Compose Hot Reload |

## Сервер (v1.1, relay)

Kotlin + Ktor Server, Docker, без базы данных. См. [backend.md](../architecture/backend.md).

## Сайт

Статический лендинг (Astro или аналог) на **GitHub Pages**. Выбор — на этапе дизайна.
Веб-приложение в будущем — Compose Multiplatform Web (Wasm), с общим кодом UI.

## Минимальные версии платформ

| Платформа | Минимум | Сборка |
|---|---|---|
| Android | API 26 (8.0) | target — последний API |
| iOS | 16 | Xcode 26+, iOS 26 SDK (обязательно для App Store с апреля 2026) |
| macOS | 12 | — |
| Windows | 10 64-bit | — |

## Что нужно для разработки

| Что | Зачем | Когда |
|---|---|---|
| **ПК на Windows + Android Studio** (плагин Kotlin Multiplatform) | общий код, Android, desktop-версия для Windows и Linux | сейчас |
| JDK 25 (LTS), через toolchain Gradle | Gradle, desktop; байткод Android — уровня 17 | сейчас |
| Android-телефон (или эмулятор) | отладка, реальная производительность, камера для QR | сейчас |
| Mac на Apple Silicon, 16+ ГБ ОЗУ | сборка iOS (Kotlin/Native) и macOS, Xcode, симулятор | есть, [трек Apple](../development-plan.md#трек-apple--параллельно-и-бесплатно) |
| iPhone | PhotoKit с iCloud, камера, производительность; установка с бесплатным Apple ID | есть |
| Apple Developer Program | TestFlight, App Store, нотаризация `.dmg` | этап «iOS и macOS», $99 в год |

На Windows Android Studio собирает и запускает Android- и desktop-версии; iOS-модули в проекте есть,
но собираются только на macOS (локально или на macOS-раннере GitHub Actions).

## iOS и macOS: что делаем на SwiftUI

Правило: интерфейс на Compose, а то, что Compose на iOS и Mac не умеет или делает хуже системы, — на SwiftUI.

- **iPhone и iPad:** навигация на SwiftUI (`TabView`, `NavigationStack`, `.toolbar`, `.sheet`) — так панели
  получают настоящий Liquid Glass. Экраны внутри — Compose через `ComposeUIViewController`.
- **macOS:** основное окно — Compose Desktop. Стекло окна, панель в строке меню и окно настроек — SwiftUI
  из Swift-библиотеки `apps/macos-native`, которую desktop-приложение вызывает через Foreign Function & Memory API.

Подробности и спайки — [ADR 0009](../architecture/adr/0009-liquid-glass-native-navigation.md).
