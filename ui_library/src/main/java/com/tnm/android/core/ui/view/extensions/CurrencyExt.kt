package com.tnm.android.core.ui.view.extensions

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

/**
 * Formats as currency for [locale].
 *
 * [fractionDigits] defaults to 0, which **rounds** the amount (1234.56 → "$1,235", HALF_EVEN).
 * That matches the dashboard-style widgets this was written for; pass 2 wherever the cents matter.
 *
 * Not @Composable: this is pure formatting. Marking it composable forced callers into a
 * composition and kept it out of ViewModels and unit tests.
 */
fun BigDecimal.formatCurrency(locale: Locale, mask: Boolean = false, fractionDigits: Int = 0): String {
    val formatter = NumberFormat.getCurrencyInstance(locale).apply {
        maximumFractionDigits = fractionDigits
        minimumFractionDigits = fractionDigits
    }

    val formatted = formatter.format(this)

    if (!mask) return formatted

    return formatted.map { char -> if (char.isDigit()) '*' else char }.joinToString("")
}
