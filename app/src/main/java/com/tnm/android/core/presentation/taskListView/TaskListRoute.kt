package com.tnm.android.core.presentation.taskListView

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun TaskListRoute(
    navController: NavHostController,
    viewModel: TaskListViewModel = hiltViewModel()
) {
    TaskListScreen(navController, viewModel)
}