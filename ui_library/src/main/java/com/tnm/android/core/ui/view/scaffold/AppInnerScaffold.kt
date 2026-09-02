package com.tnm.android.core.ui.view.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import com.tnm.android.core.ui.view.AppToolbarConfig
import com.tnm.android.core.ui.view.AppTopAppBar
import com.tnm.android.core.ui.view.extensions.withBottomPadding

/**
 * Scaffold with the app top bar, a surface-variant background and no bottom inset.
 *
 * The [PaddingValues] handed to [bodyContent] are **already applied** by the wrapping Box; they
 * are passed only so content can read the scaffold insets. Applying them again double-pads.
 */
@Composable
fun AppInnerScaffold(
    toolbarConfig: AppToolbarConfig,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    bodyContent: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                toolbarConfig = toolbarConfig,
                isDarkMode = isDarkMode,
            )
        },
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(paddingValues.withBottomPadding(layoutDirection = LocalLayoutDirection.current)),
        ) {
            bodyContent(paddingValues)
        }
    }
}
