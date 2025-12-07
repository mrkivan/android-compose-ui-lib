package com.tnm.android.core.ui.view.textField

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

data class TextInputConfig(
    val onValueChange: (String) -> Unit = {},
    val label: String? = null,
    val placeholder: String? = null,
    val isRequired: Boolean = false,
    val singleLine: Boolean = true,
    val maxLines: Int = 4,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val imeAction: ImeAction = ImeAction.Default,
    val capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    val visualTransformation: VisualTransformation = VisualTransformation.None,
    val designFlat: Boolean = false // ✅ New flag
) {
    fun resolvedMaxLines(): Int = if (singleLine) 1 else maxLines
}