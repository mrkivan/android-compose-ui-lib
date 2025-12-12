package com.tnm.android.core.ui.view.spinner.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SpinnerRowWithIcon(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    description: String? = null,
    isSelected: Boolean = false,
) {
    val itemIconColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Leading Icon
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = itemIconColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(12.dp))

        // 2. Title Text
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (!description.isNullOrEmpty()) {
                Spacer(Modifier.width(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        // 3. Selection Indicator (The Checkmark)
        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PreviewSpinnerRowWithIcon() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            // --- 1. Selected State ---
            SpinnerRowWithIcon(
                label = "Food & Dining (Selected)",
                description = "Food & Dining Tested",
                isSelected = true,
                icon = Icons.Filled.Fastfood,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // --- 2. Unselected State ---
            SpinnerRowWithIcon(
                label = "Utilities (Unselected)",
                isSelected = false,
                icon = Icons.Filled.Settings,

                )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // --- 3. Another Unselected State ---
            SpinnerRowWithIcon(
                label = "Travel",
                isSelected = false,
                icon = Icons.Filled.LocationOn,
            )
        }
    }
}