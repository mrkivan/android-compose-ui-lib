package com.tnm.android.core.ui.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun ToolbarTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvTitleSmallBold(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvTitleMedium(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvTitleMediumBold(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvBodyMedium(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TvBodyLarge(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun TvHeadlineSmall(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall
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