# Android Compose UI Library

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build](https://github.com/mrkivan/android-compose-ui-lib/actions/workflows/publish.yml/badge.svg)](https://github.com/mrkivan/android-compose-ui-lib/actions/workflows/publish.yml)
[![Maven](https://img.shields.io/maven-central/v/com.tnm.android.core/ui-library.svg?label=Maven%20Central)](https://maven.pkg.github.com/mrkivan/android-compose-ui-lib)
[![Issues](https://img.shields.io/github/issues/mrkivan/android-compose-ui-lib.svg)](https://github.com/mrkivan/android-compose-ui-lib/issues)
[![GitHub stars](https://img.shields.io/github/stars/mrkivan/android-compose-ui-lib.svg?style=social)](https://github.com/mrkivan/android-compose-ui-lib/stargazers)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue.svg)](https://kotlinlang.org/)

A lightweight Jetpack Compose library for common UI patterns in Android apps, including MVVM state management, toolbars, cards, scaffolds, dialogs,
spacers, date/time pickers, and text utilities. Designed for reusability in any Compose-based project.

## Features

- **MVVM Support**: `BaseViewModel` for easy state handling with Flows.
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
    implementation("com.tnm.android.core:ui-library:1.0.2")
}
```

Sync your project. Ensure you have all latest Jetpack Compose dependencies (e.g., `androidx.compose.material3:material3`).

## Usage

### 1. BaseDataLoadingViewModel

A generic base for ViewModels using `AppUiState<T>` (sealed class: `Loading`, `Success<T>`, `Error`). Handles data fetching via Flows with automatic
loading/error states.

#### Setup

Extend `BaseDataLoadingViewModel<T>` and provide `dataFlow(param: Any?): Flow<T>` (e.g., from a repository). Override `handleIntent` for user actions.
For example check `DashboardViewModel`

```kotlin
class DashboardViewModel : BaseDataLoadingViewModel<List<TodoTask>>() {
    override val dataFlow: Flow<List<MyData>> = flowOf(listOf(MyData()))  // Your data source

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
fun dashboardScreen(viewModel: DashboardViewModel) {
    val uiState by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.handleIntent(DashboardIntent.LoadAllData)
    }
    PlaceholderScaffold(
        toolbarConfig = AppToolbarConfig(),// TODO implement this 
        uiState = uiState.value,
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
            contentDescription = null,
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
fun myCard() {
    BaseCardView(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* Handle click */ },
        isEnable = true
    ) {
        Text("Card Content")
    }
}
```

### 4. PlaceholderScaffold

A scaffold with built-in loading, error, and retry handling for `AppUiState<T>`, example already shown in up.

### 5. ConfirmDialog

A Material3 confirmation dialog controlled by a `MutableState<Boolean>`.

```kotlin
@Composable
fun myScreen() {
    var showDialog by remember { mutableStateOf(false) }
    Button(onClick = { showDialog = true }) {
        Text("Show Dialog")
    }
    ConfirmDialog(
        title = "Confirm Action",
        message = "Are you sure?",
        confirmButtonLabel = "Yes",
        onConfirm = { showDialog = false },
        onCancel = { showDialog = false },
        showDialogState = remember { mutableStateOf(showDialog) }
    )
}
```

### 6. Spacers

Simple horizontal/vertical spacers with predefined sizes.

```kotlin
@Composable
fun myLayout() {
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
fun myTitles() {
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

## Contributing

- Fork the repo and submit PRs.
- Follow Kotlin/Compose best practices.

## License

Apache 2.0. See [LICENSE](LICENSE) for details.

For issues or features, open a GitHub issue!