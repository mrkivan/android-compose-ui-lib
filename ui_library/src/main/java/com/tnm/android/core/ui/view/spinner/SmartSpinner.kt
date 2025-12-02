package com.tnm.android.core.ui.view.spinner

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tnm.android.core.ui.view.BaseCardView
import com.tnm.android.core.ui.view.SpacerWidthLarge
import com.tnm.android.core.ui.view.TvSelectableText
import com.tnm.android.core.ui.view.spinner.config.SmartSpinnerConfig
import com.tnm.android.core.ui.view.spinner.config.SpinnerDisplayType
import com.tnm.android.core.ui.view.spinner.containers.SpinnerBottomSheet
import com.tnm.android.core.ui.view.spinner.containers.SpinnerDialog

@Composable
fun <T> SmartSpinner(
    config: SmartSpinnerConfig<T>,
    dataItems: List<T>,
    selectedItems: Set<T> = emptySet(),
    navigateToFullScreen: (() -> Unit)? = null,
    widgetContent: (@Composable (selectedItems: Set<T>) -> Unit)? = null,
    itemContent: (@Composable (item: T, selected: Boolean) -> Unit)? = null,
) {
    // internal selection state
    val selectedItemsState = remember { mutableStateOf(selectedItems) }

    val showDialog = remember { mutableStateOf(false) }
    val showBottomSheet = remember { mutableStateOf(false) }

    fun updateSelection(newSet: Set<T>) {
        selectedItemsState.value = newSet
        config.onResult(newSet)
    }

    fun showSpinner(type: SpinnerDisplayType) {
        when (type) {
            SpinnerDisplayType.Dialog -> showDialog.value = true
            SpinnerDisplayType.BottomSheet -> showBottomSheet.value = true
            SpinnerDisplayType.FullScreen -> navigateToFullScreen?.invoke()
        }
    }

    // Dialog
    if (showDialog.value) {
        SpinnerDialog(
            config = config,
            dataItems = dataItems,
            selectedItems = selectedItemsState.value,
            itemContent = itemContent,
            onDismiss = {
                updateSelection(it)
                showDialog.value = false
            }
        )
    }

    // BottomSheet
    if (showBottomSheet.value) {
        SpinnerBottomSheet(
            config = config,
            dataItems = dataItems,
            selectedItems = selectedItemsState.value,
            itemContent = itemContent,
            onDismiss = {
                updateSelection(it)
                showBottomSheet.value = false
            }
        )
    }

    // Widget UI
    if (widgetContent != null) {
        widgetContent(selectedItemsState.value)
    } else {
        BaseCardView(
            modifier = Modifier.padding(8.dp),
            onClick = { showSpinner(config.spinnerType) },
            bodyContent = {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TvSelectableText(
                        value = config.widgetPlaceholder,
                        placeholder = config.widgetTitle,
                        modifier = Modifier.weight(1f)
                    )

                    SpacerWidthLarge()

                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
    }
}