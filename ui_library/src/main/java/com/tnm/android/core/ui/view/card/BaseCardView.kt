package com.tnm.android.core.ui.view.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tnm.android.core.ui.AppConstants.MAX_WIDGET_HEIGHT
import com.tnm.android.core.ui.view.extensions.getAlpha

@Composable
fun BaseCardView(
    bodyContent: @Composable () -> Unit,
    onClick: () -> Unit,
    isEnable: Boolean,
    modifier: Modifier = Modifier,
    surfaceColor: Color = MaterialTheme.colorScheme.surface,
    minHeight: Int = MAX_WIDGET_HEIGHT
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .alpha(getAlpha(isEnable))
            .clip(MaterialTheme.shapes.medium)
            .heightIn(min = minHeight.dp)
            .clickable(
                enabled = isEnable,
                onClick = onClick,
                indication = ripple(color = MaterialTheme.colorScheme.primary),
                interactionSource = remember { MutableInteractionSource() }
            ),
        color = surfaceColor,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurface
            ) {
                bodyContent()
            }
        }
    }
}