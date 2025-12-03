package com.tnm.android.core.ui.view.textView

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.tnm.android.core.ui.view.extensions.getInactiveColor

// -------------------------------
//      TITLE TEXTS
// -------------------------------

@Composable
fun TvTitleLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge,
        color = color,
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvTitleMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        color = color,
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvTitleSmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleSmall,
        color = color,
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

// ---- Bold versions ----

@Composable
fun TvTitleLargeBold(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = color,
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvTitleMediumBold(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = color,
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvTitleSmallBold(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = color,
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

// -------------------------------
//      HEADLINE TEXTS
// -------------------------------

@Composable
fun TvHeadlineLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineLarge,
        color = color,
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvHeadlineMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineMedium,
        color = color,
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvHeadlineSmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall,
        color = color,
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

// -------------------------------
//      BODY TEXTS
//  maxLines = 2 for all body
// -------------------------------

@Composable
fun TvBodyLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = color,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvBodyMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvBodySmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

// -------------------------------
//      LABEL TEXTS
// -------------------------------

@Composable
fun TvLabelLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelLarge,
        color = color
    )
}

@Composable
fun TvLabelMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelMedium,
        color = color
    )
}

@Composable
fun TvLabelSmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        color = color
    )
}

@Composable
fun TvTitleCustomBold(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    fontSize: TextUnit = 16.sp
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvSelectableText(
    value: String?,
    placeholder: String,
    modifier: Modifier = Modifier,
    color: Color = getInactiveColor(value),
    maxLines: Int = 1
) {
    Text(
        text = value ?: placeholder,
        modifier = modifier,
        color = color,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun PlaceHolderView(
    placeholder: String,
    fontSize: TextUnit = 16.sp
) {

    Text(
        text = placeholder,
        style = LocalTextStyle.current.copy(
            fontSize = fontSize,
            color = Color.Black.copy(alpha = 0.3f)
        ),
        modifier = Modifier.fillMaxWidth()
    )
}