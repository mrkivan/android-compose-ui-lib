package com.tnm.android.core.ui.view.textField

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TextInputField(
    modifier: Modifier = Modifier,
    value: String,
    config: TextInputConfig,
    enabled: Boolean = true,
    isDarkMode: Boolean = isSystemInDarkTheme()
) {
    val focusManager = LocalFocusManager.current
    //var isFocused by remember { mutableStateOf(false) }

    // -------------------- designFlat vs card style --------------------
    val backgroundColor = if (config.designFlat) Color.Transparent else MaterialTheme.colorScheme.surface
    val shape = if (config.designFlat) RectangleShape else MaterialTheme.shapes.medium
    val elevation = if (config.designFlat) 0.dp else 2.dp
    val contentPadding = if (config.designFlat) PaddingValues(horizontal = 0.dp, vertical = 4.dp)
    else PaddingValues(horizontal = 16.dp, vertical = 14.dp)

    val textColor = if (isDarkMode) Color.White else Color.Black

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = backgroundColor,
        tonalElevation = elevation,
        shadowElevation = elevation
    ) {
        TextField(
            value = value,
            onValueChange = config.onValueChange,
            enabled = enabled,
            singleLine = config.singleLine,
            maxLines = config.resolvedMaxLines(),
            modifier = Modifier
                .fillMaxWidth()
                //.onFocusChanged { isFocused = it.isFocused }
                .padding(contentPadding),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = textColor
            ),
            label = config.label?.let { { Text(it) } },
            placeholder = config.placeholder?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(
                keyboardType = config.keyboardType,
                imeAction = config.imeAction,
                capitalization = config.capitalization
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() },
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            colors = transparentTextFieldColors(),
            visualTransformation = config.visualTransformation
        )
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES, // <- Dark mode
    name = "Dark Mode Preview"
)
@Composable
fun PreviewTextInputField() {
    MaterialTheme {
        Column(Modifier.padding(16.dp)) {
            TextInputField(
                value = "Sample Title Text",
                config = TextInputConfig(
                    placeholder = "Enter your asset name here...",
                    imeAction = ImeAction.Next,
                    designFlat = true
                ),
                modifier = Modifier.padding(16.dp)
            )
            Spacer(Modifier.height(16.dp))
            TextInputField(
                value = "New Title Text",
                config = TextInputConfig(
                    placeholder = "New asset!",
                    imeAction = ImeAction.Next,
                    designFlat = false
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}