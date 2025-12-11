package com.tnm.android.core.ui.view.extensions

import androidx.compose.runtime.Composable
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BigDecimal.formatCurrency(locale: Locale, mask: Boolean = false): String {

    val formatter = NumberFormat.getCurrencyInstance(locale).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }

    val formatted = formatter.format(this)

    if (!mask) return formatted

    val masked = formatted.map { char ->
        if (char.isDigit()) '*' else char
    }.joinToString("")

    return masked
}
