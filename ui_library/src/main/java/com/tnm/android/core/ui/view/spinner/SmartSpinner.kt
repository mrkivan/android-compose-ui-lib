package com.tnm.android.core.ui.view.spinner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
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

    // Dialog Mode
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

    // Bottom Sheet Mode
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

    // Widget UI (Trigger)
    if (widgetContent != null) {
        widgetContent(selectedItemsState.value)
    } else {

        if (config.designFlat) {
            // ---------- Flat Underline Design ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { showSpinner(config.spinnerType) },
                        indication = ripple(color = MaterialTheme.colorScheme.primary),
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .heightIn(min = 56.dp), // standard Material3 text field height
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedItemsState.value.isEmpty()) config.widgetTitle
                    else selectedItemsState.value.joinToString(", ") { it.toString() },
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selectedItemsState.value.isEmpty())
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                )

                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(24.dp)
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

        } else {
            // ---------- Card Rounded Design ----------
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(
                        onClick = { showSpinner(config.spinnerType) },
                        indication = ripple(color = MaterialTheme.colorScheme.primary),
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp, // slight shadow
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedItemsState.value.isEmpty()) config.widgetTitle
                        else selectedItemsState.value.joinToString(", ") { it.toString() },
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedItemsState.value.isEmpty())
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}