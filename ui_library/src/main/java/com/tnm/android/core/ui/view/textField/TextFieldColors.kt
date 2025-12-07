package com.tnm.android.core.ui.view.textField

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun transparentTextFieldColors() = TextFieldDefaults.colors(
    //Background (container)
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent,

    //Indicator line (bottom line)
    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
    disabledIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
    errorIndicatorColor = MaterialTheme.colorScheme.error,

    //Cursor
    cursorColor = MaterialTheme.colorScheme.primary,
    errorCursorColor = MaterialTheme.colorScheme.error,

    //Text colors
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
    errorTextColor = MaterialTheme.colorScheme.error,

    //Label colors
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
    disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    errorLabelColor = MaterialTheme.colorScheme.error,

    //Placeholder colors
    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    errorPlaceholderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
)