package com.tnm.android.core.ui.view.spinner.containers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.tnm.android.core.ui.view.spinner.config.SmartSpinnerConfig
import com.tnm.android.core.ui.view.spinner.widgets.SpinnerContent
import com.tnm.android.core.ui.view.spinner.widgets.SpinnerTopBar

@Composable
fun <T> SpinnerDialog(
    config: SmartSpinnerConfig<T>,
    dataItems: List<T>,
    selectedItems: Set<T>,
    onDismiss: (Set<T>) -> Unit,
    itemContent: (@Composable (item: T, selected: Boolean) -> Unit)? = null,
) {
    var newSelectedItems by remember { mutableStateOf(selectedItems) }

    Dialog(
        onDismissRequest = {
            onDismiss.invoke(newSelectedItems)
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(max = 800.dp)
                .fillMaxHeight(0.85f),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // Top Bar
                SpinnerTopBar(
                    config = config,
                    onDismiss = { onDismiss.invoke(newSelectedItems) },
                    modifier = Modifier.padding(top = 12.dp)
                )
                SpinnerContent(
                    config = config,
                    onSelectionChanged = {
                        newSelectedItems = it
                    },
                    dataItems = dataItems,
                    selectedItems = newSelectedItems,
                    itemContent = itemContent,
                    onDismiss = { onDismiss.invoke(newSelectedItems) },
                )
            }
        }
    }
}

