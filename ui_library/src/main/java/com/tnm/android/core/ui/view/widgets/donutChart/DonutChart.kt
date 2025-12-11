package com.tnm.android.core.ui.view.widgets.donutChart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tnm.android.core.ui.view.extensions.formatCurrency
import java.math.BigDecimal
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.min


@Composable
fun DonutChartWithTabs(
    currentSegments: List<DonutChartData>,
    locale: Locale,
    modifier: Modifier = Modifier,
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            DonutChartPressHold(currentSegments, locale)
            Spacer(modifier = Modifier.height(24.dp))
            DonutChartLegend(currentSegments)
        }
    }
}


@Composable
private fun DashboardTabs(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {

    val bg = MaterialTheme.colorScheme.surfaceVariant
    val selectedBg = MaterialTheme.colorScheme.surface
    val selectedText = MaterialTheme.colorScheme.primary
    val unselectedText = MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .background(bg, RoundedCornerShape(20.dp))
            .height(40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) selectedBg else Color.Transparent)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = selectedText),
                        ) { onTabSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) selectedText else unselectedText,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}


@Composable
private fun DonutChartLegend(segments: List<DonutChartData>) {

    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        segments.forEach { segment ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(segment.color, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = segment.label,
                    fontSize = 12.sp,
                    color = labelColor
                )
            }
        }
    }
}


@Composable
fun DonutChartPressHold(
    segments: List<DonutChartData>,
    locale: Locale,
    modifier: Modifier = Modifier
) {
    var pressedSegment by remember { mutableStateOf<DonutChartData?>(null) }

    val total = segments.fold(BigDecimal.ZERO) { acc, s -> acc + s.value }
    val displayLabel = pressedSegment?.label ?: "Total"
    val displayValue = pressedSegment?.value ?: total
    val displayText = displayValue.formatCurrency(locale)

    val centerLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val centerValueColor = MaterialTheme.colorScheme.onSurface

    val emptyRingColor = MaterialTheme.colorScheme.surfaceVariant

    val fontSize = when {
        displayText.length > 12 -> 14.sp
        displayText.length > 10 -> 16.sp
        displayText.length > 8 -> 18.sp
        else -> 20.sp
    }

    Box(contentAlignment = Alignment.Center, modifier = modifier.size(240.dp)) {
        Canvas(
            modifier = Modifier
                .size(220.dp)
                .pointerInput(segments) {
                    detectTapGestures(
                        onPress = { offset ->
                            val center = Offset(size.width / 2f, size.height / 2f)
                            val dx = offset.x - center.x
                            val dy = offset.y - center.y
                            val distance = hypot(dx, dy)

                            val strokeWidth = 40.dp.toPx()
                            val radius = min(size.width, size.height) / 2f
                            var touchedSegment: DonutChartData? = null

                            if (distance in (radius - strokeWidth / 2)..(radius + strokeWidth / 2)) {
                                var angle = atan2(dy, dx).toDegrees() + 90f
                                if (angle < 0f) angle += 360f

                                var startAngle = 0f
                                segments.forEach { segment ->
                                    val sweep = if (total == BigDecimal.ZERO) 0f
                                    else (segment.value.toFloat() / total.toFloat()) * 360f
                                    val endAngle = startAngle + sweep
                                    if (angle in startAngle..endAngle) touchedSegment = segment
                                    startAngle = endAngle
                                }
                            }

                            pressedSegment = touchedSegment
                            tryAwaitRelease()
                            pressedSegment = null
                        }
                    )
                }
        ) {
            val strokeWidth = 40.dp.toPx()
            val radius = min(size.width, size.height) / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            var startAngle = 0f

            if (segments.isEmpty()) {
                drawCircle(color = emptyRingColor, style = Stroke(width = strokeWidth))
            }

            segments.forEach { segment ->
                val sweep = if (total == BigDecimal.ZERO) 0f
                else (segment.value.toFloat() / total.toFloat()) * 360f
                drawArc(
                    color = segment.color,
                    startAngle = startAngle - 90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth),
                    size = Size(radius * 2f, radius * 2f),
                    topLeft = Offset(center.x - radius, center.y - radius)
                )
                startAngle += sweep
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = displayLabel,
                fontSize = 12.sp,
                color = centerLabelColor
            )
            Text(
                text = displayText,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = centerValueColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


// Utility: Convert radians → degrees
private fun Float.toDegrees() = Math.toDegrees(this.toDouble()).toFloat()