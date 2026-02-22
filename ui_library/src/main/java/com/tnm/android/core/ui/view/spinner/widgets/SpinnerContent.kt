package com.tnm.android.core.ui.view.spinner.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.tnm.android.core.ui.R
import com.tnm.android.core.ui.view.AppSearchBar
import com.tnm.android.core.ui.view.spinner.config.SmartSpinnerConfig
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun <T> SpinnerContent(
    config: SmartSpinnerConfig<T>,
    dataItems: List<T>,
    selectedItems: Set<T>,
    onDismiss: (Set<T>) -> Unit,
    modifier: Modifier = Modifier,
    itemContent: (@Composable (item: T, selected: Boolean) -> Unit)? = null,
    onSelectionChanged: (Set<T>) -> Unit = {},
) {
    // Local state
    var search by remember { mutableStateOf("") }
    val searchFlow = remember { MutableStateFlow("") }
    val filteredItems by produceState(initialValue = dataItems, searchFlow, dataItems) {
        searchFlow.debounce(200).collectLatest { query ->
            value = if (query.isBlank()) dataItems
            else dataItems.filter { config.rowLabel(it).contains(query, ignoreCase = true) }
        }
    }

    // Hold latest selectedItems to avoid stale closures
    val currentSelected by rememberUpdatedState(selectedItems)

    fun toggleSelection(item: T) {
        val updated = currentSelected.toMutableSet()
        if (!config.multiSelectEnable) {
            updated.clear()
            updated.add(item)
            onSelectionChanged(updated)
            onDismiss(updated)
        } else {
            if (updated.contains(item)) updated.remove(item)
            else updated.add(item)
            onSelectionChanged(updated)
        }
    }

    Column(modifier = modifier.padding(vertical = 4.dp)) {
        if (config.searchable) {
            SpinnerSearchBarSection(
                search = search,
                onSearchChange = {
                    search = it
                    searchFlow.value = it
                },
                placeholder = config.searchPlaceHolder.orEmpty()
            )
        }

        SpinnerListSection(
            items = filteredItems,
            selectedItems = currentSelected,
            config = config,
            onToggle = ::toggleSelection,
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
        placeHolder = placeholder.ifBlank { "Search..." },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )

    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
        thickness = 1.dp
    )

    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun <T> SpinnerListSection(
    items: List<T>,
    selectedItems: Set<T>,
    config: SmartSpinnerConfig<T>,
    onToggle: (T) -> Unit,
    itemContent: (@Composable (item: T, selected: Boolean) -> Unit)? = null,
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

    val isGrid = config.isGrid.first
    val gridSpanCount = config.isGrid.second.coerceAtLeast(1)

    if (isGrid) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSpanCount),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items, key = { it.hashCode() }) { item ->
                SpinnerItemView(
                    item = item,
                    isSelected = selectedItems.contains(item),
                    isGrid = true,
                    config = config,
                    onToggle = onToggle,
                    itemContent = itemContent
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(items, key = { _, item -> item.hashCode() }) { index, item ->
                SpinnerItemView(
                    item = item,
                    isSelected = selectedItems.contains(item),
                    isGrid = false,
                    config = config,
                    onToggle = onToggle,
                    itemContent = itemContent,
                    showDivider = index != items.lastIndex // remove divider for last
                )
            }
        }
    }
}

@Composable
private fun <T> SpinnerItemView(
    item: T,
    isSelected: Boolean,
    isGrid: Boolean,
    config: SmartSpinnerConfig<T>,
    onToggle: (T) -> Unit,
    itemContent: (@Composable (item: T, selected: Boolean) -> Unit)? = null,
    showDivider: Boolean = true
) {
    if (isGrid) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle(item) }
                .semantics {
                    selected = isSelected
                },
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            color = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            else MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (itemContent != null) itemContent(item, isSelected)
                else SpinnerDefaultCol(
                    label = config.rowLabel(item),
                    isSelected = isSelected,
                    isMultiSelectEnable = config.multiSelectEnable
                )
            }
        }
    } else {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle(item) }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .semantics {
                        selected = isSelected
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (itemContent != null) itemContent(item, isSelected)
                else SpinnerDefaultRow(
                    label = config.rowLabel(item),
                    isSelected = isSelected,
                    isMultiSelectEnable = config.multiSelectEnable
                )
            }

            if (showDivider) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                    thickness = 1.dp
                )
            }
        }
    }
}