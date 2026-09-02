# Android Compose UI Library

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build](https://github.com/mrkivan/android-compose-ui-lib/actions/workflows/publish.yml/badge.svg)](https://github.com/mrkivan/android-compose-ui-lib/actions/workflows/publish.yml)
[![GitHub Packages](https://img.shields.io/badge/GitHub%20Packages-3.0.0-blue.svg)](https://github.com/mrkivan/android-compose-ui-lib/packages)
[![Issues](https://img.shields.io/github/issues/mrkivan/android-compose-ui-lib.svg)](https://github.com/mrkivan/android-compose-ui-lib/issues)
[![GitHub stars](https://img.shields.io/github/stars/mrkivan/android-compose-ui-lib.svg?style=social)](https://github.com/mrkivan/android-compose-ui-lib/stargazers)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4-blue.svg)](https://kotlinlang.org/)

A lightweight Jetpack Compose library for common UI patterns in Android apps, including MVVM state management, toolbars, cards, scaffolds, dialogs,
spacers, date/time pickers, and text utilities. Designed for reusability in any Compose-based project.

## Features

- **MVI Support**: `BaseDataLoadingViewModel` + `AppUiState` for state handling with Flows.
- **Toolbar**: Configurable top app bar with icons and actions.
- **Scaffold**: Loading/error/retry placeholder scaffold.
- **Cards & Dialogs**: Clickable cards and confirmation dialogs.
- **Utils**: Spacers, text components, and date/time pickers.

## Installation
- 1st update your `settings.gradle.kts` and configs environment variables to fetch the lib from gitHub release 
- Than Add the dependency to your app-level `build.gradle.kts`:

```kotlin
//settings.gradle.kts  
maven {
    url = uri("https://maven.pkg.github.com/mrkivan/android-compose-ui-lib")
    credentials {
        username = System.getenv("GITHUB_ACTOR") ?: (extra["gpr.user"] as String?) ?: ""
        password = System.getenv("GITHUB_TOKEN") ?: (extra["gpr.key"] as String?) ?: ""
    }
}
//build.gradle.kts 
dependencies {
    implementation("com.tnm.android.core:ui-library:3.0.0")
}
```
```bash
./gradlew clean build

./gradlew clean build :ui_library:publishReleasePublicationToGitHubRepository
```

Sync your project. Ensure you have all latest Jetpack Compose dependencies (e.g., `androidx.compose.material3:material3`).

## Usage

### 1. BaseDataLoadingViewModel

A generic base for ViewModels using `AppUiState<T>` (sealed class: `Loading`, `Success<T>`, `Error`). Handles data fetching via Flows with automatic
loading/error states.

#### Setup

Extend `BaseDataLoadingViewModel<T>` and provide `dataFlow(param: Any?): Flow<T>` (e.g., from a repository). Override `handleIntent` for user actions.
Override `errorMessage(cause)` to localise what `AppUiState.Error` shows. For a working example see `TaskListViewModel` in the `:app` module.

```kotlin
class DashboardViewModel : BaseDataLoadingViewModel<List<TodoTask>>() {

    override fun dataFlow(param: Any?): Flow<List<TodoTask>> {
        val status = param as? TodoTaskStatus
        return getAllTodoTaskByStatusUseCase(status)
    }

    override fun handleIntent(intent: AppUiIntent) {
        when (intent) {
            is DashboardIntent.LoadAllData -> {
                fetchData() // no need to implement this!
            }
            // other codes ....
        }
    }
}
```

#### In UI

Collect you can use this `PlaceholderScaffold`, which will handle the `uiState`:

```kotlin
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    // collectAsStateWithLifecycle stops collecting while the app is backgrounded
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.handleIntent(DashboardIntent.LoadAllData)
    }
    PlaceholderScaffold(
        toolbarConfig = AppToolbarConfig(title = "Dashboard"),
        uiState = uiState,
        isDarkMode = isSystemInDarkTheme(),
        modifier = Modifier,
        onRetryClicked = {
            viewModel.handleIntent(DashboardIntent.LoadAllData)
        }
    ) { _, data ->
        DashboardDataView(data)
    }
}

```

### 2. AppToolbarConfig & AppTopAppBar

Configurable toolbar for app bars. Requires `@OptIn(ExperimentalMaterial3Api::class)`.

#### Config Data Class

```kotlin
AppToolbarConfig(
    title = "Todo Tasks",
    actions = listOf(
        ToolbarAction(
            icon = Icons.Default.Add,
            contentDescription = "Add task", // read by TalkBack; only null for purely decorative icons
            onClick = {
                viewModel.handleIntent(DashboardIntent.NavigateToAddTodoTask)
            }
        ),
    )
)
```

### 3. BaseCardView

A clickable card with optional enable/disable.

```kotlin
@Composable
fun MyCard() {
    BaseCardView(
        onClick = { /* Handle click */ },
        isEnable = true,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Card Content")
    }
}
```

### 4. PlaceholderScaffold

A scaffold with built-in loading, error, and retry handling for `AppUiState<T>`, example already shown in up.

### 5. ConfirmDialog

A Material3 confirmation dialog. State is hoisted: you own the `visible` flag and flip it in the callbacks.

```kotlin
@Composable
fun MyScreen() {
    var showDialog by remember { mutableStateOf(false) }
    Button(onClick = { showDialog = true }) {
        Text("Show Dialog")
    }
    ConfirmDialog(
        title = "Confirm Action",
        message = "Are you sure?",
        confirmButtonLabel = "Yes",
        visible = showDialog,
        onConfirm = {
            showDialog = false
            /* do the thing */
        },
        onDismiss = { showDialog = false }, // cancel button, back press, tap outside
    )
}
```

The 2.x overload taking a `MutableState<Boolean>` still compiles but is deprecated.

### 6. Spacers

Simple horizontal/vertical spacers with predefined sizes.

```kotlin
@Composable
fun MyLayout() {
    Column {
        Text("Item 1")
        SpacerHeightSmall()  // 4.dp height
        Text("Item 2")
        SpacerWidthMedium()  // 8.dp width (in Row)
    }
}

// Available:
// - SpacerHeightSmall/Medium/Large (4/8/16.dp)
// - SpacerWidthSmall/Medium/Large (4/8/16.dp)
```

### 7. Date & Time Pickers

Non-blocking pickers with validation callbacks. Use in a `LaunchedEffect` or button click.

#### Date Picker

```kotlin
fun onDatePick(context: Context) {
    showAppDatePicker(
        validateDate = { date -> date.isAfter(LocalDate.now()) },  // Custom validation
        onDateSelected = { date -> /* Handle selected date */ },
        context = context
    )
}
```

#### Time Picker

```kotlin
fun onTimePick(context: Context) {
    showAppTimePicker(
        validateTime = { time -> time.isAfter(LocalTime.NOON) },
        onSelectedTime = { time -> /* Handle time */ },
        selectedTime = LocalTime.of(9, 0),  // Initial time
        context = context
    )
}
```

### 8. AppText Utils

Generic text composables for consistent typography. Use wrappers for common styles or `AppText` for custom.

#### 9. Wrappers (e.g., Titles, Bodies)

```kotlin
@Composable
fun MyTitles() {
    TvTitleMediumBold(
        text = "Bold Title",
        modifier = Modifier.padding(8.dp)
    )
    TvBodyMedium(
        text = "Body text with ellipsis if long...",
        modifier = Modifier.padding(8.dp)
    )
    // Others: TvTitleSmallBold, TvBodyLarge, TvHeadlineSmall, TvTitleCustomBold, ToolbarTitle
}
```

## Migrating from 2.x

3.0.0 renames a few misspelled symbols and tightens some signatures. Old names remain as deprecated aliases and will be removed in 4.0.

| 2.x | 3.0 |
|---|---|
| `NumberInputTexField` | `NumberInputTextField` |
| `AppExpendableDropdown` | `AppExpandableDropdown` |
| `DonutChartWithTabs` | `DonutChartCard` |
| `AppConstants.ASPECT_RATION` | `AppConstants.ASPECT_RATIO` |
| `SmartSpinnerConfig(isGrid = Pair(true, 3))` | `SmartSpinnerConfig(gridColumns = 3)` |
| `ConfirmDialog(showDialogState = …)` | `ConfirmDialog(visible = …, onDismiss = …)` |
| `TextInputField(isDarkMode = …)` | parameter is ignored; colours come from `MaterialTheme` |
| `formatCurrency(locale)` | unchanged, still rounds to whole units; pass `fractionDigits = 2` to keep cents |

Parameter order changed on `ClickableRowWithIcon`, `ClickableColumnWithIcon` and `TextInputField` (required parameters now precede `modifier`); named arguments are unaffected.

## Contributing

- Fork the repo and submit PRs. CI runs `spotlessCheck`, unit tests, assembly and lint on every PR.
- Run `./gradlew spotlessApply` before pushing; formatting is enforced by ktlint via Spotless.

## License

Apache 2.0. See [LICENSE](LICENSE) for details.

For issues or features, open a GitHub issue!