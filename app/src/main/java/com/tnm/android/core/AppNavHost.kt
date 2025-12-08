package com.tnm.android.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tnm.android.core.presentation.spinnerScreen.SpinnerFullScreenScreen
import com.tnm.android.core.presentation.spinnerScreen.TestSpinnerData
import com.tnm.android.core.presentation.taskListView.TaskListRoute
import com.tnm.android.core.presentation.widgetShowcase.WidgetShowcaseRoute
import com.tnm.android.core.ui.view.spinner.config.SmartSpinnerConfig
import com.tnm.android.core.ui.view.spinner.config.SpinnerNavKeys

object AppTodoTaskDestinations {
    const val ROUTE_HOME = "home"

    const val ROUTE_ADD_TODO_TASK = "add_todo_task"

    const val ROUTE_TEST_SPINNER_SCREEN = "full_screen_spinner"
}

object NavKeys {
    const val DATA_KEY_TODO_TASK = "todo_task_object"
    const val DATA_KEY_SPINNER_CONFIG = "spinner_config"

}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = AppTodoTaskDestinations.ROUTE_HOME,
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = AppTodoTaskDestinations.ROUTE_HOME) {
            WidgetShowcaseRoute(navController)

        }
        composable(route = AppTodoTaskDestinations.ROUTE_ADD_TODO_TASK) {
            TaskListRoute(navController)
        }
        // ------------------- FullScreen Spinner -------------------
        composable(route = AppTodoTaskDestinations.ROUTE_TEST_SPINNER_SCREEN) {
            val spinnerData: List<TestSpinnerData> = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<ArrayList<TestSpinnerData>>(SpinnerNavKeys.DATA_KEY_SPINNER_ITEMS)
                ?: emptyList()

            val selectedData: Set<TestSpinnerData> = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<ArrayList<TestSpinnerData>>(SpinnerNavKeys.DATA_KEY_SELECTED_ITEMS)
                ?.toSet() ?: emptySet()

            val savedSpinnerConfig = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<SmartSpinnerConfig<TestSpinnerData>>(NavKeys.DATA_KEY_SPINNER_CONFIG)

            savedSpinnerConfig?.let {
                SpinnerFullScreenScreen(
                    config = it.copy(
                        rowLabel = { it.title }
                    ),
                    dataItems = spinnerData,
                    selectedItems = selectedData,
                    navController = navController,
                )
            }


        }
    }
}
