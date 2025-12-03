package com.tnm.android.core.presentation.widgetShowcase

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun WidgetShowcaseRoute(
    navController: NavHostController,
    screenData: WidgetShowcaseState? = null,
    viewModel: WidgetShowcaseViewModel = hiltViewModel(),
) {
    WidgetShowcaseScreen(navController, viewModel, screenData)
}