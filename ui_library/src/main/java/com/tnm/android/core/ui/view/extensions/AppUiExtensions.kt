package com.tnm.android.core.ui.view.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Picks between an "empty" and a "filled" colour for [data].
 *
 * Defaults resolve from [MaterialTheme] so the result follows the consumer's colour scheme —
 * the previous literal Gray/Black defaults rendered as invisible text in dark mode.
 */
@Composable
fun getInactiveColor(
    data: Any?,
    inActiveColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    activeColor: Color = MaterialTheme.colorScheme.onSurface,
): Color = if (data == null) inActiveColor else activeColor

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

/**
 * Copies these padding values with [bottom] replaced.
 *
 * Pass `LocalLayoutDirection.current` as [layoutDirection] from composables; the previous
 * hardcoded Ltr swapped start/end insets under RTL.
 */
fun PaddingValues.withBottomPadding(
    bottom: Dp = 0.dp,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
): PaddingValues = PaddingValues(
    start = this.calculateStartPadding(layoutDirection),
    top = this.calculateTopPadding(),
    end = this.calculateEndPadding(layoutDirection),
    bottom = bottom,
)
