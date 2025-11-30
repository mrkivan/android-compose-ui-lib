package com.tnm.android.core.todo.presentation.add

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tnm.android.core.ui.state.AppUiState
import com.tnm.android.core.ui.view.AppToolbarConfig
import com.tnm.android.core.ui.view.BaseCardView
import com.tnm.android.core.ui.view.PlaceholderScaffold
import com.tnm.android.core.ui.view.SpacerWidthLarge
import com.tnm.android.core.ui.view.TvSelectableText
import com.tnm.android.core.ui.view.TvTitleCustomBold
import com.tnm.android.core.ui.view.showAppDatePicker
import com.tnm.android.core.ui.view.showAppTimePicker
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddTodoTaskScreen(
    navController: NavHostController,
    viewModel: AddTodoTaskDataLoadingViewModel,
    screenData: AddTodoTaskState?,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {

        viewModel.handleIntent(AddTodoTaskIntent.LoadData(screenData))

        launch {
            viewModel.notificationMessage.collect { message ->
                if (!message.isNullOrEmpty()) Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        launch {
            viewModel.navigateToHome.collect { navController.navigateUp() }
        }
        launch {
            viewModel.showWarningDialog.collect {
                // TODO
            }
        }
    }

    BackHandler {
        viewModel.handleIntent(AddTodoTaskIntent.ShowWarningPopup)
    }
    PlaceholderScaffold(
        toolbarConfig = AppToolbarConfig(
            title = "Add TodoTask",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = {
                // TODO
                navController.navigateUp()
            },
        ),
        uiState = AppUiState.Success(true),
        modifier = Modifier,
        onRetryClicked = {
            viewModel.handleIntent(intent = AddTodoTaskIntent.LoadData())
        }
    ) { _, data ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                TvTitleCustomBold(text = "Select Date")
            }
            item {
                GetDatePicker(
                    validateDate = { date -> viewModel.validateDate() },
                    onDateSelected = { date ->
                        // TODO
                    })
            }
            item {
                TvTitleCustomBold(text = "Select Time")
            }
            item {
                GetTimePicker(
                    validateTime = { time ->
                        true
                    },
                    onTimeSelected = { time ->
                        // TODO
                    })
            }
        }
    }
}


@Composable
private fun GetDatePicker(
    selectedDate: LocalDate? = null,
    enableEdit: Boolean = true,
    validateDate: (LocalDate) -> Boolean,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    BaseCardView(
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
        onClick = {
            showAppDatePicker(
                validateDate = validateDate,
                onDateSelected = onDateSelected,
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
                    value = selectedDate?.toString(),
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
    selectedTime: LocalTime? = null,
    enableEdit: Boolean = true,
    validateTime: (LocalTime) -> Boolean,
    onTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current
    BaseCardView(
        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        onClick = {
            showAppTimePicker(
                validateTime = { time -> validateTime(time) },
                onSelectedTime = { time -> onTimeSelected(time) },
                selectedTime = selectedTime,
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
                    value = selectedTime?.toString(),
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
