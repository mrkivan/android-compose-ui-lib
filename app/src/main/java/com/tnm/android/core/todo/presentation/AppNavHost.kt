package com.tnm.android.core.todo.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tnm.android.core.todo.presentation.AppTodoTaskDestinations.ROUTE_ADD_TODO_TASK
import com.tnm.android.core.todo.presentation.AppTodoTaskDestinations.ROUTE_HOME
import com.tnm.android.core.todo.presentation.add.AddTodoTaskRoute
import com.tnm.android.core.todo.presentation.dashboard.DashboardRoute


object AppTodoTaskDestinations {
    const val ROUTE_HOME = "home"

    const val ROUTE_ADD_TODO_TASK = "add_todo_task"
}

object NavKeys {
    const val DATA_KEY_TODO_TASK = "todo_task_object"
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = ROUTE_HOME,
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = ROUTE_HOME) {
            DashboardRoute(navController)
        }
        composable(route = ROUTE_ADD_TODO_TASK) {
            AddTodoTaskRoute(navController)
        }
    }
}
