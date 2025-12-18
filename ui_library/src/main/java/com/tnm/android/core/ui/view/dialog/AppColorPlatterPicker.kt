package com.tnm.android.core.ui.view.dialog

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.tnm.android.core.ui.view.shape.SpacerHeightLarge
import kotlin.math.roundToInt

@Composable
fun AppColorPlatterPicker(
    initialColor: Color? = null,
    onColorSelected: (Color) -> Unit
) {
    // HSV state
    var hue by remember { mutableStateOf(0f) }          // 0-360
    var saturation by remember { mutableStateOf(1f) }   // 0-1
    var value by remember { mutableStateOf(1f) }        // 0-1

    val selectedColor = initialColor?:Color.hsv(hue, saturation, value)

    Column(modifier = Modifier.fillMaxWidth()) {

        // Color Platter using gradients
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset: Offset ->
                        val s = (offset.x / size.width).coerceIn(0f, 1f)
                        val v = 1f - (offset.y / size.height).coerceIn(0f, 1f)
                        saturation = s
                        value = v
                        onColorSelected(selectedColor)
                    }
                }
        ) {
            // Horizontal: saturation gradient
            val satGradient = Brush.horizontalGradient(
                colors = listOf(
                    Color.hsv(hue, 0f, 1f),
                    Color.hsv(hue, 1f, 1f)
                )
            )
            drawRect(brush = satGradient, size = size)

            // Vertical: value gradient overlay
            val valueGradient = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Black
                )
            )
            drawRect(brush = valueGradient, size = size)
        }

        SpacerHeightLarge()

        // Hue slider
        Text(text = "Hue: ${hue.roundToInt()}")
        Slider(
            value = hue,
            onValueChange = {
                hue = it
                onColorSelected(selectedColor)
            },
            valueRange = 0f..360f
        )

        SpacerHeightLarge()

        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            color = selectedColor
        ) {}
    }
}
