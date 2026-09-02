package com.tnm.android.core.ui.view.extensions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.util.Locale

class CurrencyExtTest {

    @Test
    fun `formats using the given locale`() {
        // Runnable at all only because formatCurrency is no longer @Composable.
        val formatted = BigDecimal("1234").formatCurrency(Locale.US)

        assertTrue(formatted.contains("1,234"))
        assertTrue(formatted.contains("$"))
    }

    @Test
    fun `drops fraction digits`() {
        val formatted = BigDecimal("1234.56").formatCurrency(Locale.US)

        assertFalse(formatted.contains("."))
    }

    @Test
    fun `masking replaces every digit but keeps the currency symbol`() {
        val masked = BigDecimal("1234").formatCurrency(Locale.US, mask = true)

        assertFalse(masked.any { it.isDigit() })
        assertTrue(masked.contains("$"))
        assertTrue(masked.contains("*"))
    }

    @Test
    fun `keeps fraction digits when asked`() {
        assertEquals("$1,234.56", BigDecimal("1234.56").formatCurrency(Locale.US, fractionDigits = 2))
    }

    @Test
    fun `formats zero`() {
        val formatted = BigDecimal.ZERO.formatCurrency(Locale.US)

        assertEquals("$0", formatted)
    }
}
