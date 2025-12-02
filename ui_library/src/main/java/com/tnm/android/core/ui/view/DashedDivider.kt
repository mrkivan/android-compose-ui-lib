package com.tnm.android.core.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DashedDivider() {
    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        val dashWidth = 10.dp.toPx()
        val gapWidth = 5.dp.toPx()
        var startX = 0f
        while (startX < size.width) {
            drawLine(
                color = Color.Gray,
                start = Offset(x = startX, y = 0f),
                end = Offset(x = startX + dashWidth, y = 0f),
                strokeWidth = size.height
            )
            startX += dashWidth + gapWidth
        }
    }
}