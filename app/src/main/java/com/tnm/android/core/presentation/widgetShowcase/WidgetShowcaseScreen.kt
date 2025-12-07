package com.tnm.android.core.presentation.widgetShowcase

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tnm.android.core.AppTodoTaskDestinations
import com.tnm.android.core.NavKeys
import com.tnm.android.core.presentation.spinnerScreen.TestSpinnerData
import com.tnm.android.core.ui.view.AppToolbarConfig
import com.tnm.android.core.ui.view.ToolbarAction
import com.tnm.android.core.ui.view.card.BaseCardView
import com.tnm.android.core.ui.view.dialog.showAppDatePicker
import com.tnm.android.core.ui.view.dialog.showAppTimePicker
import com.tnm.android.core.ui.view.scaffold.PlaceholderScaffoldWithoutState
import com.tnm.android.core.ui.view.shape.SpacerWidthLarge
import com.tnm.android.core.ui.view.spinner.SmartSpinner
import com.tnm.android.core.ui.view.spinner.config.SmartSpinnerConfig
import com.tnm.android.core.ui.view.spinner.config.SpinnerDisplayType
import com.tnm.android.core.ui.view.spinner.config.SpinnerNavKeys
import com.tnm.android.core.ui.view.textField.NumberInputConfig
import com.tnm.android.core.ui.view.textField.NumberInputTexField
import com.tnm.android.core.ui.view.textField.TextInputConfig
import com.tnm.android.core.ui.view.textField.TextInputField
import com.tnm.android.core.ui.view.textView.TvSelectableText
import com.tnm.android.core.ui.view.textView.TvTitleCustomBold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun WidgetShowcaseScreen(
    navController: NavHostController,
    viewModel: WidgetShowcaseViewModel,
    screenData: WidgetShowcaseState?,
) {
    val context = LocalContext.current

    var selectedCountry by remember { mutableStateOf<Set<String>>(emptySet()) }
    var selectedHobbies by remember { mutableStateOf<Set<String>>(emptySet()) }
    val spinnerPlaceHolder1 by remember(selectedCountry) {
        derivedStateOf {
            if (selectedCountry.isEmpty())
                "Select Country"
            else selectedCountry.joinToString(", ") { it }
        }
    }
    val spinnerPlaceHolder2 by remember(selectedHobbies) {
        derivedStateOf {
            if (selectedHobbies.isEmpty())
                "Select Hobbies"
            else selectedHobbies.joinToString(", ") { it }
        }
    }
    // ---------------------------
    // FIXED: Proper Safe Collection of SavedStateHandle Set
    // ---------------------------
    val selectedAnimalsStateFlow =
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<ArrayList<TestSpinnerData>>(
                SpinnerNavKeys.DATA_KEY_SELECTED_ITEMS,
                arrayListOf()
            )

    val selectedAnimals by (selectedAnimalsStateFlow
        ?.map { it.toSet() }
        ?.collectAsState(initial = emptySet())
        ?: remember { mutableStateOf(emptySet()) })

    // ---------------------------
    // FIXED LABEL: Only recomposes when selectedAnimals changes
    // ---------------------------
    val fullScreenSpinnerPlaceHolder by remember(selectedAnimals) {
        derivedStateOf {
            if (selectedAnimals.isEmpty())
                "Select Animals on Full Screen"
            else selectedAnimals.joinToString(", ") { it.title }
        }
    }

    val spinnerConfig = SmartSpinnerConfig<TestSpinnerData>(
        widgetTitle = "Select Animals on Full Screen",
        widgetPlaceholder = fullScreenSpinnerPlaceHolder,
        spinnerType = SpinnerDisplayType.FullScreen,
        multiSelectEnable = true,
    )

    val animals = listOf("Dog", "Cat", "Elephant", "Tiger", "Lion")

    val animalDataList: List<TestSpinnerData> =
        animals.mapIndexed { index, name ->
            TestSpinnerData(
                id = index + 1,
                title = name
            )
        }

    // ---------------------------
    // VIEWMODEL LISTENERS (UNCHANGED)
    // ---------------------------
    LaunchedEffect(Unit) {

        viewModel.handleIntent(WidgetShowcaseIntent.LoadData(screenData))
        launch {
            viewModel.navigationEvents.collect { event ->
                when (event) {
                    is WidgetShowcaseNavEvent.NavToTaskListScreen -> {
                        navController.navigate(AppTodoTaskDestinations.ROUTE_ADD_TODO_TASK)
                    }
                }
            }
        }
        launch {
            viewModel.notificationMessage.collect { message ->
                if (!message.isNullOrEmpty()) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        launch {
            viewModel.navigateToHome.collect {
                navController.navigateUp()
            }
        }

        launch {
            viewModel.showWarningDialog.collect {
                // TODO
            }
        }
    }

    BackHandler {
        viewModel.handleIntent(WidgetShowcaseIntent.ShowWarningPopup)
    }

    // ---------------------------
    // UI
    // ---------------------------
    PlaceholderScaffoldWithoutState(
        toolbarConfig = AppToolbarConfig(
            title = "Widget Showcase",
            actions = listOf(
                ToolbarAction(
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    onClick = {
                        viewModel.handleIntent(WidgetShowcaseIntent.NavigateToTaskList)
                    }
                ),
            )
        )
    ) { _ ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // ---------------------------
            // COUNTRY SPINNER
            // ---------------------------
            item {

                SmartSpinner(
                    config = SmartSpinnerConfig(
                        widgetTitle = "Select Country",
                        widgetPlaceholder = spinnerPlaceHolder1,
                        spinnerType = SpinnerDisplayType.Dialog,
                        searchable = true,
                        searchPlaceHolder = "Search by country",
                        onResult = { selectedCountry = it },
                        rowLabel = { it },
                        designFlat = true
                    ),
                    dataItems = listOf(
                        "Afghanistan", "Armenia", "Azerbaijan", "Bahrain", "Bangladesh",
                        "Bhutan", "Brunei", "Cambodia", "China", "Cyprus",
                        "Georgia", "India", "Indonesia", "Iran", "Iraq",
                        "Israel", "Japan", "Jordan", "Kazakhstan", "Kuwait",
                        "Kyrgyzstan", "Laos", "Lebanon", "Malaysia", "Maldives",
                        "Mongolia", "Myanmar", "Nepal", "North Korea", "Oman"
                    ),
                    selectedItems = selectedCountry
                )
            }

            // ---------------------------
            // HOBBIES SPINNER
            // ---------------------------
            item {

                SmartSpinner(
                    config = SmartSpinnerConfig(
                        widgetTitle = "Select Hobbies",
                        widgetPlaceholder = spinnerPlaceHolder2,
                        spinnerType = SpinnerDisplayType.BottomSheet,
                        multiSelectEnable = true,
                        onResult = { selectedHobbies = it },
                        rowLabel = { it },
                    ),
                    dataItems = listOf("Travel", "Coding", "Music", "Movies", "Cooking"),
                    selectedItems = selectedHobbies
                )
            }

            // ---------------------------
            // ANIMALS FULL SCREEN SPINNER
            // ---------------------------
            item {
                SmartSpinner(
                    config = spinnerConfig,
                    dataItems = animalDataList,
                    selectedItems = selectedAnimals,
                    navigateToFullScreen = {

                        // FIXED: savedStateHandle always stores ArrayList
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(
                                SpinnerNavKeys.DATA_KEY_SPINNER_ITEMS,
                                ArrayList(animalDataList)
                            )

                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(
                                SpinnerNavKeys.DATA_KEY_SELECTED_ITEMS,
                                ArrayList(selectedAnimals)
                            )

                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(
                                NavKeys.DATA_KEY_SPINNER_CONFIG,
                                spinnerConfig
                            )

                        navController.navigate(
                            AppTodoTaskDestinations.ROUTE_TEST_SPINNER_SCREEN
                        )
                    }
                )
            }

            // ---------------------------
            // DATE + TIME PICKER
            // ---------------------------
            item {
                TvTitleCustomBold(text = "Select Date")
            }
            item {
                NumberInputTexField(
                    modifier = Modifier.padding(16.dp),
                    initValue = BigDecimal("1000.00"),
                    config = NumberInputConfig()
                )
            }
            item {
                NumberInputTexField(
                    modifier = Modifier.padding(16.dp),
                    initValue = BigDecimal("999.00"),
                    config = NumberInputConfig(designFlat = true)
                )
            }
            item {
                TextInputField(
                    modifier = Modifier.padding(16.dp),
                    value = "Sample test input with false",
                    config = TextInputConfig()
                )
            }
            item {
                TextInputField(
                    modifier = Modifier.padding(16.dp),
                    value = "Sample test input with true",
                    config = TextInputConfig(designFlat = true)
                )
            }
            item {
                GetDatePicker(
                    validateDate = { viewModel.validateDate() },
                    onDateSelected = { date -> }
                )
            }
            item {
                TvTitleCustomBold(text = "Select Time")
            }
            item {
                GetTimePicker(
                    validateTime = { true },
                    onTimeSelected = { time -> }
                )
            }
        }
    }
}

@Composable
private fun GetDatePicker(
    previousDate: LocalDate? = null,
    enableEdit: Boolean = true,
    validateDate: (LocalDate) -> Boolean,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf(previousDate?.toString()) }

    BaseCardView(
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
        onClick = {
            showAppDatePicker(
                validateDate = validateDate,
                onDateSelected = {
                    selectedDate = it.toString()
                    onDateSelected(it)
                },
                context = context
            )
        },
        isEnable = enableEdit,
        bodyContent = {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TvSelectableText(
                    value = selectedDate,
                    placeholder = "Select Date",
                    modifier = Modifier.weight(1f)
                )

                SpacerWidthLarge()
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}


@Composable
private fun GetTimePicker(
    previousTime: LocalTime? = null,
    enableEdit: Boolean = true,
    validateTime: (LocalTime) -> Boolean,
    onTimeSelected: (LocalTime) -> Unit
) {
    var selectedTime by remember { mutableStateOf(previousTime?.toString()) }

    val context = LocalContext.current
    BaseCardView(
        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        onClick = {
            showAppTimePicker(
                validateTime = { time -> validateTime(time) },
                onSelectedTime = { time ->
                    selectedTime = time.toString()
                    onTimeSelected(time)
                },
                selectedTime = previousTime,
                context = context
            )
        },
        isEnable = enableEdit,
        bodyContent = {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TvSelectableText(
                    value = selectedTime,
                    placeholder = "Select Time",
                    modifier = Modifier.weight(1f)
                )
                SpacerWidthLarge()
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}
