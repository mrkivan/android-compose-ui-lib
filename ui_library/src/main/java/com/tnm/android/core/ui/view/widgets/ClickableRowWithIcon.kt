package com.tnm.android.core.ui.view.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun ClickableRowWithIcon(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val containerColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick.invoke() },
        colors = CardDefaults.cardColors(containerColor = containerColor),

        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,

                modifier = Modifier.weight(1f)
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewClickableColumnWithIcon() {
    Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ClickableRowWithIcon(
                    label = "Transfer",
                    icon = Icons.Filled.Settings,
                    onClick = { /* Preview click action */ },
                    modifier = Modifier.weight(1f)
                )
                ClickableRowWithIcon(
                    label = "Pay Bills",
                    icon = Icons.Filled.Info,
                    onClick = { /* Preview click action */ },
                    modifier = Modifier.weight(1f)
                )
            }
            ClickableRowWithIcon(
                label = "Deposit",
                icon = Icons.Filled.Add,
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}