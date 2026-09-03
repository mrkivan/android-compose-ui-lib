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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tnm.android.core.ui.AppConstants.MAX_WIDGET_HEIGHT
import com.tnm.android.core.ui.view.extensions.getAlpha

@Composable
fun BaseCardView(
    onClick: () -> Unit,
    isEnable: Boolean,
    modifier: Modifier = Modifier,
    surfaceColor: Color = MaterialTheme.colorScheme.surface,
    minHeight: Int = MAX_WIDGET_HEIGHT,
    // Exposed because a shadow is a separate render pass per card. In a long list or a grid that
    // is paid on every scrolled frame, and callers rendering many of these want tonal elevation
    // alone. The default keeps the previous look.
    shadowElevation: Dp = 2.dp,
    // Last so trailing-lambda syntax works: as the first parameter it bound to minHeight and
    // the documented call style did not compile.
    bodyContent: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            // Modifier.alpha always allocates a graphicsLayer, even at 1f, so it is applied only
            // when it actually does something. A grid of these was paying for a render layer per
            // card to draw them fully opaque.
            .then(if (isEnable) Modifier else Modifier.alpha(getAlpha(false)))
            .clip(MaterialTheme.shapes.medium)
            .heightIn(min = minHeight.dp)
            .clickable(
                enabled = isEnable,
                onClick = onClick,
                indication = ripple(color = MaterialTheme.colorScheme.primary),
                interactionSource = remember { MutableInteractionSource() },
            ),
        color = surfaceColor,
        tonalElevation = 2.dp,
        shadowElevation = shadowElevation,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurface,
            ) {
                bodyContent()
            }
        }
    }
}
