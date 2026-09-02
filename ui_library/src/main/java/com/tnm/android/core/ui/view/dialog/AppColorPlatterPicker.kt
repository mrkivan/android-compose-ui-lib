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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tnm.android.core.ui.R
import com.tnm.android.core.ui.view.shape.SpacerHeightLarge
import kotlin.math.roundToInt

@Composable
fun AppColorPlatterPicker(
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
    initialColor: Color? = null,
) {
    // HSV is the source of truth. Deriving it back from the emitted colour loses hue whenever
    // saturation or value hits 0 (grey and black have no hue), which snapped the slider to 0.
    // Seed once and let the controls own it from there.
    val seedHsv = remember(Unit) {
        FloatArray(3).also { hsv ->
            android.graphics.Color.colorToHSV((initialColor ?: Color.hsv(0f, 1f, 1f)).toArgb(), hsv)
        }
    }

    var hue by remember { mutableFloatStateOf(seedHsv[0]) }
    var saturation by remember { mutableFloatStateOf(seedHsv[1]) }
    var value by remember { mutableFloatStateOf(seedHsv[2]) }

    val selectedColor = Color.hsv(hue, saturation, value)

    Column(modifier = modifier.fillMaxWidth()) {
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

                        onColorSelected(Color.hsv(hue, saturation, value))
                    }
                },
        ) {
            drawRect(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color.hsv(hue, 0f, 1f),
                        Color.hsv(hue, 1f, 1f),
                    ),
                ),
                size = size,
            )

            drawRect(
                brush = Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black),
                ),
                size = size,
            )
        }

        SpacerHeightLarge()

        // Hue slider
        Text(text = stringResource(R.string.color_picker_hue, hue.roundToInt()))
        Slider(
            value = hue,
            onValueChange = {
                hue = it
                onColorSelected(Color.hsv(hue, saturation, value))
            },
            valueRange = 0f..360f,
        )

        SpacerHeightLarge()

        // Preview
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            color = selectedColor,
        ) {}
    }
}
