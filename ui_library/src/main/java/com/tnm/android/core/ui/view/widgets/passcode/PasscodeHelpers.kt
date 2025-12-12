package com.tnm.android.core.ui.view.widgets.passcode

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val PASSCODE_SIZE = 6

@Composable
fun PasscodeDots(count: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

        val activeColor = MaterialTheme.colorScheme.primary
        val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

        repeat(6) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        color = if (it < count) activeColor else inactiveColor,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun Keypad(
    onDigit: (String) -> Unit,
    onDelete: () -> Unit,
    onComplete: () -> Unit
) {
    val context = LocalContext.current

    val keys = listOf(
        "1", "2", "3",
        "4", "5", "6",
        "7", "8", "9",
        "bio", "0", "del"
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        keys.chunked(3).forEach { rowKeys ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                rowKeys.forEach { key ->
                    when (key) {
                        "del" -> KeypadButton(icon = Icons.AutoMirrored.Filled.Backspace) {
                            onDelete()
                        }

                        "bio" -> KeypadButton(icon = Icons.Default.Fingerprint) {
                            Toast.makeText(
                                context,
                                "This feature is not implemented yet!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> KeypadButton(label = key) {
                            onDigit(key)
                            onComplete()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KeypadButton(
    label: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    val buttonColor = MaterialTheme.colorScheme.surfaceVariant
    val textColor = MaterialTheme.colorScheme.onSurface
    val iconColor = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(buttonColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when {
            label != null -> {
                Text(
                    text = label,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }

            icon != null -> {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
