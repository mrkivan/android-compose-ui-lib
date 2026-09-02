package com.tnm.android.core

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
import com.tnm.android.core.theme.AppTodoTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTodoTaskTheme {
                val navController = rememberNavController()

                // Insets are deliberately zeroed here. Every screen renders its own Scaffold
                // (PlaceholderScaffold, AppInnerScaffold, …) which applies the system-bar insets
                // itself, and Modifier.padding does not consume them — so letting this outer
                // Scaffold pad as well offsets every top bar by the status-bar height twice.
                // This container exists only to host the NavHost.
                Scaffold(
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    modifier = Modifier.fillMaxSize(),
                ) { paddingValues ->
                    AppNavHost(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                    )
                }
            }
        }
    }
}
