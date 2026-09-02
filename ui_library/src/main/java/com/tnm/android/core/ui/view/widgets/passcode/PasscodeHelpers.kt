package com.tnm.android.core.ui.view.widgets.passcode

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tnm.android.core.ui.R

const val PASSCODE_SIZE = 6

private val KEYPAD_BUTTON_SIZE = 70.dp

@Composable
fun PasscodeDots(count: Int, modifier: Modifier = Modifier, size: Int = PASSCODE_SIZE) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val activeColor = MaterialTheme.colorScheme.primary
        val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

        repeat(size) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        color = if (it < count) activeColor else inactiveColor,
                        shape = CircleShape,
                    ),
            )
        }
    }
}

/**
 * Numeric passcode keypad.
 *
 * [entryCount] is the number of digits already entered; [onComplete] fires once that reaches
 * [passcodeSize] — previously it fired after every keypress, so callers saw "complete" on the
 * first digit. Pass [onBiometric] to enable the fingerprint key; it is hidden when null rather
 * than showing a dead button.
 */
@Composable
fun Keypad(
    onDigit: (String) -> Unit,
    onDelete: () -> Unit,
    onComplete: () -> Unit,
    entryCount: Int,
    modifier: Modifier = Modifier,
    passcodeSize: Int = PASSCODE_SIZE,
    onBiometric: (() -> Unit)? = null,
) {
    val keys = listOf(
        "1", "2", "3",
        "4", "5", "6",
        "7", "8", "9",
        "bio", "0", "del",
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        keys.chunked(3).forEach { rowKeys ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(),
            ) {
                rowKeys.forEach { key ->
                    when (key) {
                        "del" -> KeypadButton(
                            icon = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = stringResource(R.string.passcode_delete),
                            onClick = onDelete,
                        )

                        "bio" -> if (onBiometric != null) {
                            KeypadButton(
                                icon = Icons.Default.Fingerprint,
                                contentDescription = stringResource(R.string.passcode_biometric),
                                onClick = onBiometric,
                            )
                        } else {
                            // Keep the grid aligned without offering a button that does nothing.
                            Spacer(modifier = Modifier.size(KEYPAD_BUTTON_SIZE))
                        }

                        else -> KeypadButton(
                            label = key,
                            onClick = {
                                onDigit(key)
                                // == not >=: past the limit this would fire again on every keypress.
                                if (entryCount + 1 == passcodeSize) onComplete()
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KeypadButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    icon: ImageVector? = null,
    contentDescription: String? = null,
) {
    val buttonColor = MaterialTheme.colorScheme.surfaceVariant
    val textColor = MaterialTheme.colorScheme.onSurface
    val iconColor = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier
            .size(KEYPAD_BUTTON_SIZE)
            .clip(CircleShape)
            .background(buttonColor)
            // Role.Button so TalkBack announces these as buttons; a bare clickable Box is
            // announced as plain text.
            .semantics { role = Role.Button }
            .clickable(
                onClickLabel = contentDescription ?: label,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        when {
            label != null -> {
                Text(
                    text = label,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                )
            }

            icon != null -> {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp),
                )
            }
        }
    }
}
