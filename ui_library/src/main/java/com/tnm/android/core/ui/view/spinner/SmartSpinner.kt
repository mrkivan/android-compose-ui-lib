package com.tnm.android.core.ui.view.spinner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            FlatSpinnerRow(
                selectedItems = selectedItemsState.value,
                title = config.widgetTitle,
                maxHeight = config.maxHeight,
                onClick = { showSpinner(config.spinnerType) },
                rowLabel = config.rowLabel
            )
        } else {
            CardSpinnerRow(
                selectedItems = selectedItemsState.value,
                title = config.widgetTitle,
                maxHeight = config.maxHeight,
                onClick = { showSpinner(config.spinnerType) },
                rowLabel = config.rowLabel
            )
        }
    }
}

@Composable
private fun <T> FlatSpinnerRow(
    selectedItems: Set<T>,
    title: String,
    maxHeight: Int,
    onClick: () -> Unit,
    rowLabel: (T) -> String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = ripple(color = MaterialTheme.colorScheme.primary),
                interactionSource = remember { MutableInteractionSource() }
            )
            .height(maxHeight.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        SpinnerRowContent(
            selectedItems = selectedItems,
            title = title,
            rowLabel = rowLabel,
            paddingInside = 16.dp
        )
        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(
            thickness = 3.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
    }
}

@Composable
private fun <T> CardSpinnerRow(
    selectedItems: Set<T>,
    title: String,
    maxHeight: Int,
    onClick: () -> Unit,
    rowLabel: (T) -> String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(
                onClick = onClick,
                indication = ripple(color = MaterialTheme.colorScheme.primary),
                interactionSource = remember { MutableInteractionSource() }
            )
            .height(maxHeight.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        SpinnerRowContent(
            selectedItems = selectedItems,
            title = title,
            rowLabel = rowLabel,
            paddingInside = 16.dp // same padding as before
        )
    }
}

@Composable
private fun <T> SpinnerRowContent(
    selectedItems: Set<T>,
    title: String,
    rowLabel: (T) -> String,
    paddingInside: Dp = 8.dp
) {
    val textColor = if (selectedItems.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingInside),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (selectedItems.isEmpty()) title
            else selectedItems.joinToString(", ") { rowLabel(it) },
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            color = textColor,
            maxLines = 2,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis
        )

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}