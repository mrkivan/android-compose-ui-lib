package com.tnm.android.core.todo.presentation.add

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun AddTodoTaskRoute(
    navController: NavHostController,
    screenData: AddTodoTaskState? = null,
    viewModel: AddTodoTaskDataLoadingViewModel = hiltViewModel(),
) {
    AddTodoTaskScreen(navController, viewModel, screenData)
}