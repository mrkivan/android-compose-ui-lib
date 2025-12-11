package com.tnm.android.core.ui.view.dropdown

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AppExpendableDropdown(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    headerContent: @Composable (T, @Composable () -> Unit, Boolean) -> Unit,
    itemContent: @Composable (T, Boolean) -> Unit,
    sort: ((List<T>) -> List<T>) = { it }
) {
    var expanded by remember { mutableStateOf(false) }
    var currentItem by remember(selectedItem) { mutableStateOf(selectedItem) }

    val sortedItems = remember(items) { sort(items) }

    val firstItem = sortedItems.firstOrNull() ?: selectedItem

    if (sortedItems.size <= 1) {
        headerContent(
            firstItem,          // Position 1: Item
            { /* None */ },     // Position 2: TrailingIconComposable
            true                // Position 3: isSelected
        )
        return
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        // --- ANCHOR / HEADER
        headerContent(
            currentItem,        // Position 1: Item
            {                   // Position 2: TrailingIconComposable
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse dropdown" else "Expand dropdown"
                )
            },
            true                // Position 3: isSelected
        )

        // --- DROPDOWN MENU ---
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            sortedItems.forEachIndexed { index, item ->
                val isSelected = item == currentItem

                DropdownMenuItem(
                    // Render the content using the ItemContent slot (Invoked with positional arguments)
                    text = {
                        itemContent(
                            item,       // Position 1: Item
                            isSelected  // Position 2: isSelected
                        )
                    },
                    onClick = {
                        if (item != currentItem) {
                            currentItem = item
                            onItemSelected(item)
                        }
                        expanded = false
                    },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                if (index < sortedItems.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}