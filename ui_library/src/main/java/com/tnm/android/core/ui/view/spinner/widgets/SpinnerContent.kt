package com.tnm.android.core.ui.view.spinner.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tnm.android.core.ui.R
import com.tnm.android.core.ui.view.AppSearchBar
import com.tnm.android.core.ui.view.spinner.config.SmartSpinnerConfig

@Composable
fun <T> SpinnerContent(
    config: SmartSpinnerConfig<T>,
    dataItems: List<T>,
    selectedItems: Set<T>,
    itemContent: (@Composable (item: T, selected: Boolean) -> Unit)?,
    onDismiss: (Set<T>) -> Unit,
    modifier: Modifier = Modifier,
    onSelectionChanged: (Set<T>) -> Unit = {},
) {
    var search by remember { mutableStateOf("") }
    var filterItems by remember { mutableStateOf(dataItems) }

    fun toggleSelection(item: T) {
        val current = selectedItems.toMutableSet()

        if (!config.multiSelectEnable) {
            current.clear()
            current.add(item)
            onSelectionChanged(current)
            onDismiss.invoke(current)
        } else {
            if (current.contains(item)) current.remove(item)
            else current.add(item)
            onSelectionChanged(current)
        }
    }

    LaunchedEffect(search, dataItems) {
        filterItems =
            if (search.isBlank()) dataItems
            else dataItems.filter {
                config.rowLabel(it).lowercase().startsWith(search.lowercase())
            }
    }

    Column(modifier = modifier.fillMaxSize()) {

        if (config.searchable) {
            SpinnerSearchBarSection(
                search = search,
                onSearchChange = { search = it },
                placeholder = config.searchPlaceHolder.orEmpty()
            )
        }

        SpinnerListSection(
            items = filterItems,
            selectedItems = selectedItems,
            config = config,
            onToggle = { toggleSelection(it) },
            itemContent = itemContent
        )
    }
}

@Composable
private fun SpinnerSearchBarSection(
    search: String,
    onSearchChange: (String) -> Unit,
    placeholder: String
) {
    AppSearchBar(
        search = search,
        onSearchChange = onSearchChange,
        placeHolder = placeholder,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )

    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
    )

    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun <T> SpinnerListSection(
    items: List<T>,
    selectedItems: Set<T>,
    config: SmartSpinnerConfig<T>,
    onToggle: (T) -> Unit,
    itemContent: (@Composable (item: T, selected: Boolean) -> Unit)?,
) {
    if (items.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.search_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    LazyColumn {
        items(items) { item ->
            SpinnerItemRow(
                item = item,
                isSelected = selectedItems.contains(item),
                config = config,
                onToggle = onToggle,
                itemContent = itemContent
            )
        }
    }
}

@Composable
private fun <T> SpinnerItemRow(
    item: T,
    isSelected: Boolean,
    config: SmartSpinnerConfig<T>,
    onToggle: (T) -> Unit,
    itemContent: (@Composable (item: T, selected: Boolean) -> Unit)?,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle(item) }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (itemContent != null) {
                itemContent(item, isSelected)
            } else {
                SpinnerDefaultRow(
                    label = config.rowLabel(item),
                    isSelected = isSelected,
                    isMultiSelectEnable = config.multiSelectEnable
                )
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}