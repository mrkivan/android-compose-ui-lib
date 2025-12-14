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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    Column(modifier = modifier.padding(vertical = 4.dp)) {
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
            items(items) { item ->
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
            items(items) { item ->
                SpinnerItemView(
                    item = item,
                    isSelected = selectedItems.contains(item),
                    isGrid = false,
                    config = config,
                    onToggle = onToggle,
                    itemContent = itemContent
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
    itemContent: (@Composable (item: T, selected: Boolean) -> Unit)?,
) {
    if (isGrid) {
        // GRID ITEM
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle(item) },
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            color = if (isSelected)
                Color(0xFF4A4458).copy(alpha = 0.40f)
            else
                MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (itemContent != null) {
                    itemContent(item, isSelected)
                } else {
                    SpinnerDefaultCol(
                        label = config.rowLabel(item),
                        isSelected = isSelected,
                        isMultiSelectEnable = config.multiSelectEnable
                    )
                }
            }
        }
    } else {
        // 📄 LIST ITEM
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
                thickness = 1.dp
            )
        }
    }
}
