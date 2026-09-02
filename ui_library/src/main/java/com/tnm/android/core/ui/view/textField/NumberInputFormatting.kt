package com.tnm.android.core.ui.view.textField

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Pure text/number helpers behind [NumberInputTextField].
 *
 * They live apart from the composable so they can be unit tested on the JVM — the digit
 * normalisation below shipped broken once precisely because nothing covered it.
 */

/** Maps Arabic-Indic and Bengali digits onto ASCII, leaving every other character alone. */
internal fun normalizeToEnglish(text: String): String = buildString(text.length) {
    text.forEach { char ->
        append(
            when (char) {
                // '0' + offset keeps this a Char. Char - Char yields an Int, and appending
                // that Int would write its code point ("53") instead of the digit.
                in '٠'..'٩' -> '0' + (char - '٠')
                in '০'..'৯' -> '0' + (char - '০')
                else -> char
            },
        )
    }
}

/**
 * Digits only, at most [maxLength] integer digits and two decimals.
 *
 * With [withoutDecimal] the decimal point is rejected outright: letting it through meant
 * DecimalFormat("#,###") rounded "12.5" to "12" on screen while the callback received 12.5.
 */
@Suppress("RegExpSimplifiable")
internal fun isValidInput(raw: String, maxLength: Int, withoutDecimal: Boolean = false): Boolean {
    if (raw.isEmpty()) return true
    val regex = if (withoutDecimal) {
        Regex("^\\d{0,$maxLength}$")
    } else {
        Regex("^\\d{0,$maxLength}(\\.\\d{0,2})?$")
    }
    return regex.matches(raw)
}

/**
 * Groups the integer part while the user is still typing, preserving a trailing "." and any
 * partially typed decimals so the caret does not jump.
 */
internal fun formatFlexible(raw: String, withoutDecimal: Boolean): String {
    val symbols = DecimalFormatSymbols(Locale.US)

    if (withoutDecimal) {
        val format = DecimalFormat("#,###", symbols)
        return raw.toBigDecimalOrNull()?.let { format.format(it) } ?: raw
    }

    val parts = raw.split(".")
    val intPart = parts[0]
    val decimalPart = parts.getOrNull(1)

    val format = DecimalFormat("#,###", symbols)
    val formattedInt = intPart.toBigIntegerOrNull()
        ?.let { format.format(it) }
        ?: ""

    return buildString {
        append(formattedInt)
        if (raw.contains(".")) {
            append(".")
            if (decimalPart != null) append(decimalPart)
        }
    }
}

/** Parses [raw], clamping to [maxValue] when one is given. Returns null if [raw] is not a number. */
internal fun parseBigDecimal(raw: String, maxValue: BigDecimal?): BigDecimal? {
    val parsed = raw.toBigDecimalOrNull() ?: return null
    return if (maxValue == null || parsed <= maxValue) parsed else maxValue
}
