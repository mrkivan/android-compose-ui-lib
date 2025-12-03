package com.tnm.android.core.presentation.taskListView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tnm.android.core.data.TodoTaskStatus
import com.tnm.android.core.domain.TodoTask
import com.tnm.android.core.ui.view.AppToolbarConfig
import com.tnm.android.core.ui.view.card.BaseCardView
import com.tnm.android.core.ui.view.scaffold.PlaceholderScaffold
import com.tnm.android.core.ui.view.shape.SpacerWidthMedium
import com.tnm.android.core.ui.view.textView.TvBodyMedium

@Composable
fun TaskListScreen(
    navController: NavHostController,
    viewModel: TaskListViewModel
) {
    val uiState = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(TaskListIntent.LoadAllData)

    }

    PlaceholderScaffold(
        toolbarConfig = AppToolbarConfig(
            title = "Todo Tasks List",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = { navController.navigateUp() },
        ),

        uiState = uiState.value,
        modifier = Modifier,
        onRetryClicked = {
            viewModel.handleIntent(TaskListIntent.LoadAllData)
        }
    ) { _, data ->
        TaskListDataView(data)
    }
}

@Composable
fun TaskListDataView(data: List<TodoTask>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(
            items = data,
            key = { it.id }
        ) { item ->
            BaseCardView(
                onClick = {
                    // TODO
                },
                bodyContent = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)  // Tighter spacing for balance
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 1,
                                text = item.taskName,
                                color = Color.Black,
                                fontSize = 16.sp,  // Slightly smaller for better fit
                                overflow = TextOverflow.Ellipsis
                            )
                            TvBodyMedium(
                                text = item.taskDescription,
                            )
                        }
                        SpacerWidthMedium()
                        val imageVector = when (item.status) {
                            TodoTaskStatus.PENDING -> Icons.Default.Schedule
                            TodoTaskStatus.FAILED -> Icons.Default.Error


                            TodoTaskStatus.COMPLETED -> Icons.Default.CheckCircle


                            TodoTaskStatus.CANCELLED -> Icons.Default.Cancel
                        }
                        Icon(
                            imageVector = imageVector,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            )
        }
    }
}