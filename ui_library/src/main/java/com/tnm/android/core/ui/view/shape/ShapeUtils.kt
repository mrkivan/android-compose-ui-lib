package com.tnm.android.core.ui.view.shape

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SpacerHeightSmall() = Spacer(modifier = Modifier.height(4.dp))

@Composable
fun SpacerHeightMedium() = Spacer(modifier = Modifier.height(8.dp))

@Composable
fun SpacerHeightLarge() = Spacer(modifier = Modifier.height(16.dp))

@Composable
fun SpacerWidthSmall() = Spacer(modifier = Modifier.width(4.dp))

@Composable
fun SpacerWidthMedium() = Spacer(modifier = Modifier.width(8.dp))

@Composable
fun SpacerWidthLarge() = Spacer(modifier = Modifier.width(16.dp))

@Composable
fun CircleWithNumber(number: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(
                MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ),
    ) {
        Text(
            text = number.toString(),
            color = Color.White,
            modifier = Modifier.align(Alignment.Center),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }


}


@Composable
fun CircularPercentageProgress(
    progress: Float, // 0f..1f
    size: Dp = 50.dp,
    strokeWidth: Dp = 8.dp,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    percentageTextStyle: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        // Background circle
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = backgroundColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            // Progress arc
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360 * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        // Percentage text
        Text(
            text = "${(progress * 100).toInt()}%",
            style = percentageTextStyle
        )
    }
}