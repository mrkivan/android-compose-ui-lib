package com.tnm.android.core.ui.view.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tnm.android.core.ui.R
import com.tnm.android.core.ui.view.shape.SpacerHeightLarge

@Composable
fun ColorPickerDialog(
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit,
    dialogLabel: String,
    btnLabel: String,
    modifier: Modifier = Modifier,
    initialColor: Color? = null,
) {
    // Hold selected color INSIDE dialog
    var currentColor by remember {
        mutableStateOf(initialColor ?: Color.Red)
    }

    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            // Back press must dismiss: with no cancel affordance the dialog was inescapable
            // unless the user committed to a colour.
            dismissOnBackPress = true,
        ),
        onDismissRequest = onDismiss,
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = dialogLabel,
                    style = MaterialTheme.typography.titleMedium,
                )

                SpacerHeightLarge()

                AppColorPlatterPicker(
                    initialColor = currentColor,
                    onColorSelected = { color ->
                        currentColor = color // always updated
                    },
                )

                SpacerHeightLarge()

                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.color_picker_cancel))
                    }

                    Button(
                        onClick = {
                            onColorSelected(currentColor)
                            onDismiss()
                        },
                    ) {
                        Text(btnLabel)
                    }
                }
            }
        }
    }
}
