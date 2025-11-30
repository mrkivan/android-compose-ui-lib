package com.tnm.android.core.todo.presentation.dashboard

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun DashboardRoute(
    navController: NavHostController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    DashboardScreen(navController, viewModel)
}