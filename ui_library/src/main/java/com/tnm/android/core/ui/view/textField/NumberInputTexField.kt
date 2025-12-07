package com.tnm.android.core.ui.view.textField

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.text.DecimalFormat


// ---------------------------- NumberInputTexField ----------------------------
@Composable
fun NumberInputTexField(
    modifier: Modifier = Modifier,
    initValue: BigDecimal? = null,
    maxValue: BigDecimal? = null,
    config: NumberInputConfig,
) {
    val pattern = if (config.withoutDecimal) "#,###" else "#,###.00"
    val decimalFormatAlwaysTwo = remember { DecimalFormat(pattern) }

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = if (initValue != null) decimalFormatAlwaysTwo.format(initValue)
                    .orEmpty() else "",
                selection = TextRange(Int.MAX_VALUE)
            )
        )
    }

    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(initValue) {
        textFieldValue = initValue?.let {
            TextFieldValue(
                formatFlexible(it.toPlainString(), config.withoutDecimal),
                TextRange(Int.MAX_VALUE)
            )
        } ?: if (config.isRequired) {
            val zeroFormatted = decimalFormatAlwaysTwo.format(BigDecimal.ZERO)
            TextFieldValue(zeroFormatted, TextRange(zeroFormatted.length))
        } else {
            TextFieldValue("")
        }
    }

    LaunchedEffect(isFocused) {
        if (!isFocused) {
            val raw = textFieldValue.text.replace(",", "")
            val valueToFormat =
                if (raw.isEmpty() && config.isRequired) BigDecimal.ZERO else raw.toBigDecimalOrNull()
            if (valueToFormat != null) {
                val formatted = decimalFormatAlwaysTwo.format(valueToFormat)
                textFieldValue = TextFieldValue(formatted, TextRange(formatted.length))
            } else if (!config.isRequired) {
                textFieldValue = TextFieldValue("") // allow empty if not required
            }
        }
    }

    // -------------------- designFlat vs card style --------------------
    val backgroundColor = if (config.designFlat) Color.Transparent else MaterialTheme.colorScheme.surface
    val shape = if (config.designFlat) RectangleShape else MaterialTheme.shapes.medium
    val elevation = if (config.designFlat) 0.dp else 2.dp
    val contentPadding = if (config.designFlat) PaddingValues(horizontal = 0.dp, vertical = 4.dp)
    else PaddingValues(horizontal = 16.dp, vertical = 14.dp)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = backgroundColor,
        tonalElevation = elevation,
        shadowElevation = elevation
    ) {
        TextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                val raw = newValue.text.replace(",", "")

                if (!isValidInput(raw, config.maxLength)) return@TextField

                if (raw.isEmpty()) {
                    textFieldValue = TextFieldValue("", selection = TextRange(0))
                    return@TextField
                }

                val formatted = formatFlexible(raw, config.withoutDecimal)

                textFieldValue = TextFieldValue(
                    text = formatted,
                    selection = TextRange(formatted.length) // cursor always at end
                )

                parseBigDecimal(raw, maxValue)?.let { config.onValueChange(it) }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .padding(contentPadding),
            textStyle = LocalTextStyle.current.copy(
                textAlign = config.textAlign,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
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

// ---------------------------- Helpers ----------------------------
@Suppress("RegExpSimplifiable")
private fun isValidInput(raw: String, maxLength: Int): Boolean {
    if (raw.isEmpty()) return true
    val regex = Regex("^\\d{0,$maxLength}(\\.\\d{0,2})?$")
    return regex.matches(raw)
}

private fun formatFlexible(raw: String, withoutDecimal: Boolean): String {
    if (withoutDecimal) {
        return DecimalFormat("#,###").format(raw.toBigDecimal())
    }
    val parts = raw.split(".")
    val intPart = parts[0]
    val decimalPart = parts.getOrNull(1)

    val formattedInt = intPart.toBigIntegerOrNull()
        ?.let { DecimalFormat("#,###").format(it) }
        ?: ""

    return buildString {
        append(formattedInt)
        if (raw.contains(".")) {
            append(".")
            if (decimalPart != null) append(decimalPart)
        }
    }
}

private fun parseBigDecimal(raw: String, maxValue: BigDecimal?): BigDecimal? {
    val parsed = raw.toBigDecimalOrNull() ?: return null
    Log.d("CurrencyTextInput", "parsed=$parsed maxValue: $maxValue")
    return if (maxValue == null || parsed <= maxValue) parsed else maxValue
}

// ---------------------------- Composables ----------------------------
@Composable
private fun PlaceHolderView(config: NumberInputConfig) {
    Text(
        text = config.placeholder.orEmpty(),
        style = LocalTextStyle.current.copy(
            textAlign = config.textAlign,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.3f)
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun TrailingLabelView(trailingLabel: String) {
    Text(
        text = trailingLabel,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            color = Color.Gray
        )
    )
}

// ---------------------------- Preview ----------------------------
@Preview(showBackground = true)
@Composable
fun PreviewCurrencyTextInput() {
    MaterialTheme {
        Column(Modifier.padding(16.dp)) {
            NumberInputTexField(
                initValue = BigDecimal("1000.00"),
                config = NumberInputConfig(designFlat = true)
            )
            Spacer(Modifier.height(16.dp))
            NumberInputTexField(
                initValue = BigDecimal("1000.00"),
                config = NumberInputConfig(designFlat = false)
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES, // <- Dark mode
    name = "Dark Mode Preview"
)
@Composable
fun PreviewCurrencyTextInputDark() {
    MaterialTheme {
        NumberInputTexField(
            modifier = Modifier.padding(16.dp),
            initValue = BigDecimal("1000.00"),
            config = NumberInputConfig()
        )
    }
}