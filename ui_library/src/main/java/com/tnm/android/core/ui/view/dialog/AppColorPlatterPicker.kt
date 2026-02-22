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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.tnm.android.core.ui.view.shape.SpacerHeightLarge
import kotlin.math.roundToInt

@Composable
fun AppColorPlatterPicker(
    initialColor: Color? = null,
    onColorSelected: (Color) -> Unit
) {
    var hue by remember { mutableStateOf(0f) }
    var saturation by remember { mutableStateOf(1f) }
    var value by remember { mutableStateOf(1f) }

    var selectedColor by remember {
        mutableStateOf(initialColor ?: Color.hsv(0f, 1f, 1f))
    }

    // Initialize HSV from initialColor ONCE
    LaunchedEffect(initialColor) {
        initialColor?.let { color ->
            val hsv = FloatArray(3)
            android.graphics.Color.colorToHSV(color.toArgb(), hsv)
            hue = hsv[0]
            saturation = hsv[1]
            value = hsv[2]
            selectedColor = color
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        // Color palette
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val s = (offset.x / size.width).coerceIn(0f, 1f)
                        val v = 1f - (offset.y / size.height).coerceIn(0f, 1f)

                        saturation = s
                        value = v

                        selectedColor = Color.hsv(hue, saturation, value)
                        onColorSelected(selectedColor) // always called
                    }
                }
        ) {
            drawRect(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color.hsv(hue, 0f, 1f),
                        Color.hsv(hue, 1f, 1f)
                    )
                ),
                size = size
            )

            drawRect(
                brush = Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black)
                ),
                size = size
            )
        }

        SpacerHeightLarge()

        // Hue slider
        Text(text = "Hue: ${hue.roundToInt()}")
        Slider(
            value = hue,
            onValueChange = {
                hue = it
                selectedColor = Color.hsv(hue, saturation, value)
                onColorSelected(selectedColor) // always called
            },
            valueRange = 0f..360f
        )

        SpacerHeightLarge()

        // Preview
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            color = selectedColor
        ) {}
    }
}