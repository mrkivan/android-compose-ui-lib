package com.tnm.android.core.ui.view.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun <T> GenericTabView(
    mainData: T,
    tabs: List<String>,
    modifier: Modifier = Modifier,
    tabKey: Any,

    pageContent: @Composable (selectedTabIndex: Int, data: T) -> Unit
) {
    var selectedTabIndex by remember(tabKey) { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        SecondaryTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title, style = MaterialTheme.typography.titleSmall) },
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            pageContent(selectedTabIndex, mainData)
        }
    }
}

@Preview
@Composable
fun GenericTabViewPreview() {
    data class SofData(
        val id: String,
        val position: Int,
        val name: String
    )

    val card = SofData("123", 1, "John Doe")
    val tabs = listOf("Account Info", "Unbilled", "Billed")

    Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                GenericTabView(
                    mainData = card,
                    tabs = tabs,
                    tabKey = card.id, // Use the unique ID for the key
                    modifier = Modifier
                ) { selectedTabIndex, currentCard ->

                    when (selectedTabIndex) {
                        0 -> Text("AccountInfoTagView")
                        1 -> Text("AccountUnBilledTab")
                        2 -> Text("AccountBilledTab")
                    }
                }
            }
        }
    }
}