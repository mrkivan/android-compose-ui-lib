package com.tnm.android.core.ui.view.textField

import android.content.res.Configuration
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// ---------------------------- NumberInputTextField ----------------------------

/** Kept for source compatibility with 2.x; the name was misspelled. */
@Deprecated(
    message = "Renamed to NumberInputTextField.",
    replaceWith = ReplaceWith("NumberInputTextField(modifier, initValue, maxValue, config)"),
)
@Composable
fun NumberInputTexField(
    modifier: Modifier = Modifier,
    initValue: BigDecimal? = null,
    maxValue: BigDecimal? = null,
    config: NumberInputConfig,
) = NumberInputTextField(modifier, initValue, maxValue, config)

@Composable
fun NumberInputTextField(
    modifier: Modifier = Modifier,
    initValue: BigDecimal? = null,
    maxValue: BigDecimal? = null,
    config: NumberInputConfig,
) {
    val symbols = remember { DecimalFormatSymbols(Locale.US) }
    val pattern = if (config.withoutDecimal) "#,##0" else "#,##0.00"
    // Keyed on the pattern: an unkeyed remember kept the first formatter after withoutDecimal changed.
    val decimalFormatAlwaysTwo = remember(pattern) { DecimalFormat(pattern, symbols) }

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = if (initValue != null) {
                    decimalFormatAlwaysTwo.format(initValue)
                        .orEmpty()
                } else {
                    ""
                },
                selection = TextRange(Int.MAX_VALUE),
            ),
        )
    }

    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(initValue) {
        textFieldValue = initValue?.let {
            TextFieldValue(
                formatFlexible(it.toPlainString(), config.withoutDecimal),
                TextRange(Int.MAX_VALUE),
            )
        } ?: TextFieldValue("")
    }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            // Select all text on focus to allow overwriting the entire value when typing starts
            textFieldValue = textFieldValue.copy(
                selection = TextRange(0, textFieldValue.text.length),
            )
        } else {
            val raw = textFieldValue.text.replace(",", "")
            val valueToFormat =
                if (raw.isEmpty() && config.isRequired) BigDecimal.ZERO else raw.toBigDecimalOrNull()
            if (valueToFormat != null) {
                val formatted = decimalFormatAlwaysTwo.format(valueToFormat)
                textFieldValue = TextFieldValue(formatted, TextRange(formatted.length))
            } else if (!config.isRequired) {
                textFieldValue = TextFieldValue("")
            }
        }
    }

    // -------------------- designFlat vs card style --------------------
    val backgroundColor = if (config.designFlat) Color.Transparent else MaterialTheme.colorScheme.surface
    val shape = if (config.designFlat) RectangleShape else MaterialTheme.shapes.medium
    val elevation = if (config.designFlat) 0.dp else 2.dp
    val contentPadding = if (config.designFlat) {
        PaddingValues(horizontal = 0.dp, vertical = 4.dp)
    } else {
        PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = backgroundColor,
        tonalElevation = elevation,
        shadowElevation = elevation,
    ) {
        TextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                // FIX START: Normalize Bangla/Arabic inputs to English immediately
                val normalizedText = normalizeToEnglish(newValue.text)
                // FIX END

                val raw = normalizedText.replace(",", "")

                if (!isValidInput(raw, config.maxLength, config.withoutDecimal)) return@TextField

                if (raw.isEmpty()) {
                    textFieldValue = TextFieldValue("", selection = TextRange(0))
                    config.onValueChange(BigDecimal.ZERO)
                    return@TextField
                }

                val formatted = formatFlexible(raw, config.withoutDecimal)

                textFieldValue = TextFieldValue(
                    text = formatted,
                    selection = TextRange(formatted.length), // cursor always at end
                )

                parseBigDecimal(raw, maxValue)?.let { config.onValueChange(it) }
            },
            keyboardOptions = KeyboardOptions(
                // No decimal key at all when decimals are not accepted.
                keyboardType = if (config.withoutDecimal) KeyboardType.Number else KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() },
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .padding(contentPadding),
            textStyle = LocalTextStyle.current.copy(
                localeList = LocaleList("en-US"), // This helps with rendering fonts
                textAlign = config.textAlign,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            ),
            placeholder = {
                config.placeholder?.let { PlaceHolderView(config) }
            },
            trailingIcon = {
                config.trailingLabel?.let { TrailingLabelView(it) }
            },
            colors = transparentTextFieldColors(),
        )
    }
}

// ---------------------------- Composable ----------------------------
@Composable
private fun PlaceHolderView(config: NumberInputConfig) {
    val placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    Text(
        text = config.placeholder.orEmpty(),
        style = LocalTextStyle.current.copy(
            textAlign = config.textAlign,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = placeholderColor,
        ),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun TrailingLabelView(trailingLabel: String) {
    val trailingColor = MaterialTheme.colorScheme.onSurfaceVariant

    Text(
        text = trailingLabel,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            color = trailingColor,
        ),
    )
}

// ---------------------------- Preview ----------------------------
@Preview(showBackground = true)
@Composable
private fun PreviewCurrencyTextInput() {
    MaterialTheme {
        Column(Modifier.padding(16.dp)) {
            NumberInputTextField(
                initValue = BigDecimal("1000.00"),
                config = NumberInputConfig(designFlat = true),
            )
            Spacer(Modifier.height(16.dp))
            NumberInputTextField(
                initValue = BigDecimal("1000.00"),
                config = NumberInputConfig(designFlat = false),
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES, // <- Dark mode
    name = "Dark Mode Preview",
)
@Composable
private fun PreviewCurrencyTextInputDark() {
    MaterialTheme {
        NumberInputTextField(
            modifier = Modifier.padding(16.dp),
            initValue = BigDecimal("1000.00"),
            config = NumberInputConfig(),
        )
    }
}
