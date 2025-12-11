package com.tnm.android.core.ui.view.widgets.donutChart

import androidx.compose.ui.graphics.Color
import java.math.BigDecimal

data class DonutChartData(
    val label: String,
    val value: BigDecimal,
    val color: Color
)