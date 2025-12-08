package com.tnm.android.core.ui.view

import java.math.BigDecimal
import java.text.DecimalFormat

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

const val MAX_WIDGET_HEIGHT = 88