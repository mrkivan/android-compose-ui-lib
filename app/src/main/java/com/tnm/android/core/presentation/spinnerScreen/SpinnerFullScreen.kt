package com.tnm.android.core.presentation.spinnerScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tnm.android.core.ui.R
import com.tnm.android.core.ui.view.AppToolbarConfig
import com.tnm.android.core.ui.view.AppTopAppBar
import com.tnm.android.core.ui.view.ToolbarAction
import com.tnm.android.core.ui.view.spinner.config.SmartSpinnerConfig
import com.tnm.android.core.ui.view.spinner.config.SpinnerNavKeys
import com.tnm.android.core.ui.view.spinner.widgets.SpinnerContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SpinnerFullScreenScreen(
    config: SmartSpinnerConfig<T>,
    dataItems: List<T>,
    selectedItems: Set<T>,
    navController: NavHostController,
    itemContent: (@Composable (item: T, selected: Boolean) -> Unit)? = null,
) {
    var newSelectedItems by remember { mutableStateOf(selectedItems) }

    Scaffold(
        topBar = {
            AppTopAppBar(
                toolbarConfig = AppToolbarConfig(
                    title = config.widgetTitle,
                    navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                    onNavigationClick = {
                        navController.navigateUp()
                    },
                    actions = listOfNotNull(
                        if (config.multiSelectEnable) ToolbarAction(
                            icon = Icons.Default.Check,
                            contentDescription = stringResource(R.string.done),
                            onClick = {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(
                                        SpinnerNavKeys.DATA_KEY_SELECTED_ITEMS,
                                        ArrayList(newSelectedItems)
                                    )
                                navController.popBackStack()
                            }
                        ) else null,
                    ),
                ),
                isDarkMode = isSystemInDarkTheme(),
            )
        },
    ) { innerPadding ->
        // Main Screen Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopStart
        ) {
            SpinnerContent(
                config = config,
                onSelectionChanged = {
                    newSelectedItems = it
                },
                onDismiss = { data ->
                    // Send data back
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            SpinnerNavKeys.DATA_KEY_SELECTED_ITEMS,
                            ArrayList(data)
                        )
                    navController.popBackStack()
                },
                dataItems = dataItems,
                selectedItems = newSelectedItems,
                itemContent = itemContent,
                modifier = Modifier.padding(16.dp)
            )
        }

    }

}