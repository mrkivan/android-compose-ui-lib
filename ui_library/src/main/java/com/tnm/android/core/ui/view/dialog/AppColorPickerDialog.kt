package com.tnm.android.core.ui.view.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tnm.android.core.ui.view.shape.SpacerHeightLarge

@Composable
fun showColorPickerDialog(
    initialColor: Color? = null,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit,
    dialogLabel: String,
    btnLabel: String,
    modifier: Modifier = Modifier
) {
    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        ),
        onDismissRequest = onDismiss,
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = dialogLabel,
                    style = MaterialTheme.typography.titleMedium
                )

                SpacerHeightLarge()

                AppColorPlatterPicker(
                    initialColor = initialColor,
                    onColorSelected = { color ->
                        onColorSelected(color)
                    }
                )

                SpacerHeightLarge()

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(btnLabel)
                }
            }
        }
    }
}