# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this repo is

A publishable Jetpack Compose UI library (`com.tnm.android.core:ui-library`) plus a demo app that consumes it.

- `:ui_library` — the shipped artifact. Android library, namespace `com.tnm.android.core.ui`. No DI framework, no Room, no networking, no navigation. Only Compose (via the BOM), core-ktx, lifecycle-runtime and lifecycle-viewmodel (`api`, because `BaseDataLoadingViewModel` extends `ViewModel`). `SpinnerNavKeys` are plain string constants; the navigation itself belongs to the consumer.
- `:app` — sample/showcase app, namespace `com.tnm.android.core`. Depends on `project(":ui_library")` and adds Hilt + Room. It exists to exercise library widgets, not to be shipped.

Anything added to `:ui_library` becomes public API of a published Maven artifact. Adding a dependency there forces it on every consumer — keep that module lean.

## Build / test commands

```bash
./gradlew build
```

```bash
./gradlew :ui_library:assembleRelease
```

```bash
./gradlew :app:installDebug
```

```bash
./gradlew test
```

Single module / single test:

```bash
./gradlew :ui_library:testDebugUnitTest --tests "com.tnm.android.core.ui.ExampleUnitTest"
```

Instrumented (needs device/emulator):

```bash
./gradlew :app:connectedDebugAndroidTest
```

Lint:

```bash
./gradlew :app:lintDebug
```

Formatting is enforced by Spotless + ktlint from the root `build.gradle.kts` (`spotlessCheck` runs as part of `check`). Fix formatting before pushing:

```bash
./gradlew spotlessApply
```

CI (`.github/workflows/ci.yml`) runs `spotlessCheck`, unit tests, assembly and lint on every PR and push to `main`. JDK 17 toolchain, `minSdk 26`, `compileSdk 37`.

## Publishing a release

Publishing goes to GitHub Packages, not Maven Central (the README badge is misleading).

CI (`.github/workflows/publish.yml`) runs `spotlessCheck` + unit tests and then `:ui_library:publishReleasePublicationToGitHubRepository` on any `v*` tag push, authenticating with the job's own `GITHUB_TOKEN` (`permissions: packages: write`). No PAT secret is needed or configured. Nothing checks that the tag matches the version in `ui_library/build.gradle.kts` — do that by hand.

Local publish needs `gpr.user` / `gpr.key` in `~/.gradle/gradle.properties` (or `GITHUB_ACTOR` / `GITHUB_TOKEN` env):

```bash
./gradlew :ui_library:publishReleasePublicationToGitHubRepository
```

A version bump touches **four** places that must stay in sync — miss one and the artifact, sample, and docs disagree:
1. `ui_library/build.gradle.kts` → `publications { create<MavenPublication>("release") { version = ... } }`
2. `app/build.gradle.kts` → `versionName`
3. `app/build.gradle.kts` → `versionCode` (increment)
4. `README.md` install snippet

## Architecture: the MVI contract

The library defines a small state/intent contract; the app implements it. Understanding these four types explains most of the codebase.

- `AppUiState<T>` (`ui/state/`) — sealed interface: `Loading` | `Success<T>(data)` | `Error(message)`.
- `AppUiIntent` (`ui/intent/`) — empty marker interface. Each screen declares its own `sealed class XIntent : AppUiIntent`.
- `BaseDataLoadingViewModel<T>` (`ui/viewmodel/`) — owns `StateFlow<AppUiState<T>>`. Subclass implements `dataFlow(param: Any?): Flow<T>` and `handleIntent(intent)`. Calling the protected `fetchData(param)` wires `onStart → Loading`, `catch → Error`, `collect → Success` automatically and cancels any previous collection first (hot sources never complete). Override `errorMessage(cause)` to control the Error text; never emit states manually unless bypassing the flow (`setSuccess`/`setError` are protected escape hatches).
- `PlaceholderScaffold` (`ui/view/scaffold/`) — the UI half: takes `AppUiState<T>`, renders `LoadingView` / `ErrorView` + retry / `bodyContent(padding, data)`.

The `param: Any?` on `dataFlow` is deliberately untyped; implementations cast it (`param as? TodoTaskStatus`). Intents carry the typed value.

Screen wiring convention in `:app` (mirror it for new screens): `XIntent.kt`, `XRoute.kt` (thin, `hiltViewModel()` only), `XScreen.kt` (composable UI), `XViewModel.kt`. Routes are registered in `AppNavHost.kt` with string constants in `AppTodoTaskDestinations`.

## Library conventions

- **Theming is explicit, not ambient.** Composables that render chrome (`AppTopAppBar`, `PlaceholderScaffold`, `AppInnerScaffold`) take an `isDarkMode: Boolean` parameter rather than reading the system theme. Dark top bar color is hardcoded in `AppConstants.DARK_MODE_TOPBAR_COLOR`.
- **Config data classes over long parameter lists.** `AppToolbarConfig`/`ToolbarAction`, `SmartSpinnerConfig<T>`, `TextInputConfig`, `NumberInputConfig`. New widgets with more than a few knobs should follow this.
- **Text goes through `ui/view/textView/TextViewUtils.kt`** (`TvTitleMedium`, `TvBodyLarge`, …) — thin `Text` wrappers bound to `MaterialTheme.typography`. Don't inline raw `Text` with hand-set styles in library code.
- **Spacers live in `ui/view/shape/`** (`SpacerHeightSmall/Medium/Large` = 4/8/16.dp).
- `SmartSpinner` is the largest component: one entry point, three presentations via `SpinnerDisplayType` (`Dialog`, `BottomSheet`, `FullScreen`). Dialog/BottomSheet render in place; **FullScreen is a navigation callback** — the host app must own the destination and hand results back through `savedStateHandle` using `SpinnerNavKeys`. `SmartSpinnerConfig` is `@Parcelize` so it can cross that navigation boundary; the two lambdas (`rowLabel`, `onResult`) are `@IgnoredOnParcel` and **must be re-supplied via `.copy()` after restore** (see `AppNavHost.kt`). Grid layout is `gridColumns: Int?`; the old `isGrid: Pair` is deprecated but still honoured through `effectiveGridColumns`. Selection state inside `SmartSpinner` is plain `remember`, deliberately not `rememberSaveable`: a `Set` of Parcelable-only items passes Compose's saveable check and then crashes the Bundle write.
- **Deprecated aliases are kept for one major version.** 3.0 renamed misspelled public symbols (`NumberInputTexField`, `AppExpendableDropdown`, `DonutChartWithTabs`, `ASPECT_RATION`) and the `MutableState`-taking `ConfirmDialog`; the old names delegate to the new ones and carry `@Deprecated` with `ReplaceWith`. Remove them in 4.0, not before.
- **Scaffold `PaddingValues` are already applied.** `PlaceholderScaffold`, `PlaceholderScaffoldWithoutState` and `AppInnerScaffold` pad their content Box and then also pass the values to the body lambda. Applying them again double-pads; the README example ignores them with `_`.
- **Encryption is consumer-injected.** `ui/security/` ships the `EncryptionManager` interface, `AndroidKeyStoreCryptoManager`, and `EncryptedString`/`EncryptedBigDecimal` value classes. The value classes resolve through the `EncryptionInitializer` singleton, which throws unless the app calls `EncryptionInitializer.initialize(manager)` in its `Application` class.

## Skills that apply here

Installed skills relevant to this repo. Load the specific one rather than reasoning from memory — AGP 9 and Compose have both moved recently enough that recalled defaults are often a version behind.

| Situation | Skill |
|---|---|
| Reviewing a diff, PR, or module before merge | `android-code-review` |
| Touching `build.gradle.kts`, the version catalog, or an AGP-9 warning | `android-skills:agp-9-upgrade` |
| Adding real tests to either module (both are stubs today) | `android-skills:testing-setup` |
| Insets, system bars, `enableEdgeToEdge()` in `MainActivity` | `android-skills:edge-to-edge` |
| Theming, `MaterialTheme`, the `Tv*` text wrappers, dark-mode colors | `android-skills:styles` |
| Release size, R8 rules, `consumer-rules.pro` | `android-skills:r8-analyzer` |
| Navigation work beyond the current Nav2 `NavHost` | `android-skills:navigation-3` |
| Exported components, intents, the `ui/security/` package | `android-skills:android-intent-security` |
| Converting Java, or Kotlin tooling migrations | `kotlin-agent-skills:*` |

`kotlin-lsp` is installed for `.kt`/`.kts` code intelligence (binary at `/opt/homebrew/bin/kotlin-lsp`).

Two repo-specific review angles the generic checklist will not raise on its own:

- **`:ui_library` is a published artifact.** New `public` symbols are permanent API and a semver commitment — default to `internal` unless consumers need it. Adding a required parameter to an existing public composable is a breaking change; give it a default.
- **Dependencies added to `:ui_library` are forced on every consumer.** `implementation` unless callers need the type on their classpath.

## Known rough edges

- No Compose UI tests and no instrumented tests in either module. Unit coverage exists for `BaseDataLoadingViewModel`, the number-input formatting helpers, `formatCurrency`, and the app's data/domain/ViewModel layers (fakes, not mocks).
- `InputNumberRowWithLabel` takes `Float` for an amount that is otherwise `BigDecimal` everywhere. Public API; change it in 4.0.
- `BaseDataLoadingViewModel.errorMessage` falls back to a hardcoded English string because the ViewModel has no `Context`; consumers who localise must override it.
- Showcase-app strings in `WidgetShowcaseScreen` are hardcoded; only `TaskListScreen` uses resources.
- `:app` reaches `androidx.navigation.compose` only transitively through `hilt-navigation-compose`; it has no direct `navigation-compose` dependency.
