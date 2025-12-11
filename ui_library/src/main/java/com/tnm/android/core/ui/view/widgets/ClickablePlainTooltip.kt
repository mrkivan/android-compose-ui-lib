package com.tnm.android.core.ui.view.widgets

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickablePlainTooltip(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable (showTooltip: () -> Unit) -> Unit
) {
    val scope = rememberCoroutineScope()

    val tooltipState = rememberTooltipState(isPersistent = false)

    val showTooltip: () -> Unit = {
        scope.launch {
            tooltipState.show()
        }
    }

    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(positioning = TooltipAnchorPosition.Above),
        tooltip = {
            PlainTooltip {
                Text(label)
            }
        },
        state = tooltipState
    ) {
        content(showTooltip)
    }
}