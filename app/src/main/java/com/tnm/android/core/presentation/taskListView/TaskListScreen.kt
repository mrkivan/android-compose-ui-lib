package com.tnm.android.core.presentation.taskListView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tnm.android.core.data.TodoTaskStatus
import com.tnm.android.core.domain.TodoTask
import com.tnm.android.core.ui.view.AppToolbarConfig
import com.tnm.android.core.ui.view.card.BaseCardView
import com.tnm.android.core.ui.view.scaffold.PlaceholderScaffold

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
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(
            items = data,
            key = { _, item -> item.id }
        ) { index, item ->

            val isFirst = index == 0
            val isLast = index == data.lastIndex

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = if (isFirst) 16.dp else 8.dp,
                        bottom = if (isLast) 16.dp else 8.dp
                    )
            ) {
                BaseCardView(
                    isEnable = true,
                    onClick = {
                        // TODO
                    },
                    modifier = Modifier,
                    bodyContent = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = item.taskName,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,

                                    // 🔥 FIX: Dynamic color for dark/light theme
                                    color = MaterialTheme.colorScheme.onSurface,

                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Text(
                                    text = item.taskDescription,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            val icon = when (item.status) {
                                TodoTaskStatus.PENDING -> Icons.Default.Schedule
                                TodoTaskStatus.FAILED -> Icons.Default.Error
                                TodoTaskStatus.COMPLETED -> Icons.Default.CheckCircle
                                TodoTaskStatus.CANCELLED -> Icons.Default.Cancel
                            }

                            Icon(
                                imageVector = icon,
                                contentDescription = null,

                                // 🔥 FIX: Use theme color instead of default
                                tint = MaterialTheme.colorScheme.primary,

                                modifier = Modifier.size(42.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}
