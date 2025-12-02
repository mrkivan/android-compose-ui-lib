package com.tnm.android.core.ui.view.spinner.containers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tnm.android.core.ui.view.spinner.config.SmartSpinnerConfig
import com.tnm.android.core.ui.view.spinner.widgets.SpinnerContent
import com.tnm.android.core.ui.view.spinner.widgets.SpinnerTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SpinnerBottomSheet(
    config: SmartSpinnerConfig<T>,
    dataItems: List<T>,
    selectedItems: Set<T>,
    onDismiss: (Set<T>) -> Unit,
    itemContent: (@Composable (item: T, selected: Boolean) -> Unit)? = null,
) {
    var newSelectedItems by remember { mutableStateOf(selectedItems) }

    ModalBottomSheet(
        onDismissRequest = { onDismiss.invoke(newSelectedItems) },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        tonalElevation = 4.dp
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(top = 4.dp),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 2.dp
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
