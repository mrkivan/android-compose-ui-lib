package com.tnm.android.core.ui.view.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tnm.android.core.ui.view.textField.NumberInputConfig
import com.tnm.android.core.ui.view.textField.NumberInputTexField

@Composable
fun InputNumberRowWithLabel(
    label: String,
    value: Float,
    maxValue: Float,
    onValueChange: (Float) -> Unit,
    suffix: String,
) {
    var textInput by remember { mutableStateOf(value.toBigDecimal()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        NumberInputTexField(
            modifier = Modifier.weight(1f),
            initValue = textInput,
            maxValue = maxValue.toBigDecimal(),
            config = NumberInputConfig(
                trailingLabel = suffix,
                maxLength = maxValue.toInt().toString().length,
                onValueChange = { newValue ->
                    textInput = newValue
                    onValueChange(newValue.toFloat())
                },
                placeholder = "0",
                withoutDecimal = true,
                isRequired = true
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditLimitScreenPreview() {
    MaterialTheme {
        InputNumberRowWithLabel(
            label = "Test Label",
            value = 100.toFloat(),
            maxValue = 1000.toFloat(),
            onValueChange = {},
            suffix = "USD",
        )
    }
}
