package com.tnm.android.core.ui.view.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tnm.android.core.ui.R

/**
 * Material3 confirmation dialog, shown while [visible] is true.
 *
 * State is hoisted: the caller flips [visible] to false from [onConfirm] and [onDismiss].
 * [onDismiss] fires for the cancel button, the back press and a tap outside.
 */
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmButtonLabel: String,
    visible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    cancelButtonLabel: String = stringResource(R.string.btn_cancel),
) {
    if (!visible) return

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Text(text = message, style = MaterialTheme.typography.bodyMedium)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmButtonLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(cancelButtonLabel)
            }
        },
    )
}

/** 2.x signature. Passing a MutableState hands the dialog write access to the caller's state. */
@Deprecated(
    message = "Hoist the state: pass visible: Boolean and flip it in onConfirm/onDismiss.",
    replaceWith = ReplaceWith(
        "ConfirmDialog(title, message, confirmButtonLabel, showDialogState.value, onConfirm, onCancel ?: {}, modifier)",
    ),
)
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmButtonLabel: String,
    onConfirm: () -> Unit,
    showDialogState: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onCancel: (() -> Unit)? = null,
) {
    ConfirmDialog(
        title = title,
        message = message,
        confirmButtonLabel = confirmButtonLabel,
        visible = showDialogState.value,
        onConfirm = {
            showDialogState.value = false
            onConfirm()
        },
        onDismiss = {
            showDialogState.value = false
            onCancel?.invoke()
        },
        modifier = modifier,
    )
}
