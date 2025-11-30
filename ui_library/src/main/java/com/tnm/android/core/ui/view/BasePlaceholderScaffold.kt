package com.tnm.android.core.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tnm.android.core.ui.R
import com.tnm.android.core.ui.state.AppUiState

@Composable
fun <T> PlaceholderScaffold(
    toolbarConfig: AppToolbarConfig,
    uiState: AppUiState<T>,
    modifier: Modifier = Modifier,
    onRetryClicked: () -> Unit = {},
    bodyContent: @Composable (PaddingValues, T) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopAppBar(toolbarConfig = toolbarConfig)
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (uiState) {
                is AppUiState.Loading -> LoadingView()
                is AppUiState.Error -> ErrorView(message = uiState.message, onRetryClicked = { onRetryClicked.invoke() })
                is AppUiState.Success -> bodyContent(paddingValues, uiState.data)
            }
        }
    }
}

// Reusable Loading View
@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

// Reusable Error View
@Composable
fun ErrorView(
    message: String,
    modifier: Modifier = Modifier,
    showReloadBtn: Boolean = true,
    onRetryClicked: () -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (showReloadBtn) {
                Button(onClick = onRetryClicked) {
                    Text(text = stringResource(R.string.btn_retry))
                    SpacerWidthSmall()
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.btn_retry),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}