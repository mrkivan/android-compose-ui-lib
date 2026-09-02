package com.tnm.android.core.ui.view.textField

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class NumberInputFormattingTest {

    // --- normalizeToEnglish ---------------------------------------------------------------

    @Test
    fun `converts bengali digits to ascii digits`() {
        // Regression: this returned "53" (the ASCII code point of '5') because the branch
        // produced an Int instead of a Char, silently corrupting every entered amount.
        assertEquals("5", normalizeToEnglish("৫"))
        assertEquals("1234567890", normalizeToEnglish("১২৩৪৫৬৭৮৯০"))
    }

    @Test
    fun `converts arabic indic digits to ascii digits`() {
        assertEquals("5", normalizeToEnglish("٥"))
        assertEquals("1234567890", normalizeToEnglish("١٢٣٤٥٦٧٨٩٠"))
    }

    @Test
    fun `leaves ascii digits and separators untouched`() {
        assertEquals("1,234.56", normalizeToEnglish("1,234.56"))
    }

    @Test
    fun `normalizes mixed scripts within one string`() {
        assertEquals("12.50", normalizeToEnglish("১2.٥0"))
    }

    @Test
    fun `returns empty string unchanged`() {
        assertEquals("", normalizeToEnglish(""))
    }

    // --- isValidInput ---------------------------------------------------------------------

    @Test
    fun `accepts empty input so the field can be cleared`() {
        assertTrue(isValidInput("", maxLength = 5))
    }

    @Test
    fun `accepts up to two decimal places`() {
        assertTrue(isValidInput("123.45", maxLength = 5))
        assertFalse(isValidInput("123.456", maxLength = 5))
    }

    @Test
    fun `rejects more integer digits than maxLength`() {
        assertTrue(isValidInput("12345", maxLength = 5))
        assertFalse(isValidInput("123456", maxLength = 5))
    }

    @Test
    fun `rejects non numeric characters`() {
        assertFalse(isValidInput("12a", maxLength = 5))
        assertFalse(isValidInput("-12", maxLength = 5))
    }

    @Test
    fun `rejects a decimal point when decimals are disabled`() {
        // Regression: "12.5" passed validation and then DecimalFormat("#,###") rounded it to
        // "12" on screen while the callback still received 12.5.
        assertFalse(isValidInput("12.", maxLength = 5, withoutDecimal = true))
        assertFalse(isValidInput("12.5", maxLength = 5, withoutDecimal = true))
        assertTrue(isValidInput("12", maxLength = 5, withoutDecimal = true))
    }

    @Test
    fun `still accepts decimals when they are enabled`() {
        assertTrue(isValidInput("12.5", maxLength = 5, withoutDecimal = false))
    }

    // --- formatFlexible -------------------------------------------------------------------

    @Test
    fun `groups thousands`() {
        assertEquals("1,234", formatFlexible("1234", withoutDecimal = true))
        assertEquals("1,234,567", formatFlexible("1234567", withoutDecimal = true))
    }

    @Test
    fun `keeps a trailing decimal point while typing`() {
        // Stripping the "." here would fight the user mid-entry.
        assertEquals("1,234.", formatFlexible("1234.", withoutDecimal = false))
        assertEquals("1,234.5", formatFlexible("1234.5", withoutDecimal = false))
    }

    @Test
    fun `returns raw input when it is not a number`() {
        assertEquals("abc", formatFlexible("abc", withoutDecimal = true))
    }

    // --- parseBigDecimal ------------------------------------------------------------------

    @Test
    fun `clamps to maxValue`() {
        assertEquals(BigDecimal("100"), parseBigDecimal("150", BigDecimal("100")))
        assertEquals(BigDecimal("50"), parseBigDecimal("50", BigDecimal("100")))
    }

    @Test
    fun `passes through when no maxValue is given`() {
        assertEquals(BigDecimal("150"), parseBigDecimal("150", null))
    }

    @Test
    fun `returns null for unparseable input`() {
        assertNull(parseBigDecimal("abc", null))
    }
}
