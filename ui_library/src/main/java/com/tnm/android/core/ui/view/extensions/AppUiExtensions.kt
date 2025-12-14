package com.tnm.android.core.ui.view.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.text.DecimalFormat

fun getInactiveColor(
    data: Any?,
    inActiveColor: Color = Color.Gray,
    activeColor: Color = Color.Black
): Color {
    return if (data == null) inActiveColor else activeColor
}

fun BigDecimal?.formatWithComma(): String {
    if (this == null) return ""
    val format = DecimalFormat("#,###")
    return format.format(this)
}

fun BigDecimal?.formatWithCommaDecimal(): String {
    if (this == null) return ""
    val format = DecimalFormat("#,###.##")
    return format.format(this)
}

fun getAlpha(isEnable: Boolean): Float = (if (isEnable) 1f else 0.5f)

fun PaddingValues.withBottomPadding(bottom: Dp = 0.dp): PaddingValues {
    return PaddingValues(
        start = this.calculateStartPadding(LayoutDirection.Ltr),
        top = this.calculateTopPadding(),
        end = this.calculateEndPadding(LayoutDirection.Ltr),
        bottom = bottom
    )
}
