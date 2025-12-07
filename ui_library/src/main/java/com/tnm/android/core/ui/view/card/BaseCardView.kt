package com.tnm.android.core.ui.view.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun BaseCardView(
    bodyContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnable: Boolean = true
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (isEnable) 1f else 0.4f)
            .clickable(
                enabled = isEnable,
                onClick = onClick
            ),
        shape = AppCardDefaults.shape,
        elevation = AppCardDefaults.elevation(),
        colors = AppCardDefaults.colors()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            bodyContent()
        }
    }
}
