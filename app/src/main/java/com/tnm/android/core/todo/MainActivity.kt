package com.tnm.android.core.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.tnm.android.core.todo.presentation.AppNavHost
import com.tnm.android.core.todo.theme.AppTodoTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            AppTodoTaskTheme {
                val navController = rememberNavController()
                navController.enableOnBackPressed(true)

                Scaffold(
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    AppNavHost(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController
                    )
                }
            }
        }
    }
}