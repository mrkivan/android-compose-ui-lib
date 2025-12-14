package com.tnm.android.core.ui.view.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tnm.android.core.ui.view.AppToolbarConfig
import com.tnm.android.core.ui.view.AppTopAppBar

@Composable
fun PlaceholderScaffoldWithoutState(
    toolbarConfig: AppToolbarConfig,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    bodyContent: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                toolbarConfig = toolbarConfig,
                isDarkMode = isDarkMode,
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            bodyContent(paddingValues)
        }
    }
}