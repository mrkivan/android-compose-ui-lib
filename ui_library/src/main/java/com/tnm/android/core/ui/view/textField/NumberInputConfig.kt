package com.tnm.android.core.ui.view.textField

import androidx.compose.ui.text.style.TextAlign
import java.math.BigDecimal

// ---------------------------- NumberInputConfig ----------------------------
data class NumberInputConfig(
    val maxLength: Int = 10,
    val onValueChange: (BigDecimal) -> Unit = {},
    val trailingLabel: String? = "USD",
    val placeholder: String? = "0.00",
    val withoutDecimal: Boolean = false,
    val isRequired: Boolean = false,
    val textAlign: TextAlign = TextAlign.End,
    val designFlat: Boolean = false // NEW: flat style
)