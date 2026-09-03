package com.tnm.android.core.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tnm.android.core.ui.R
import com.tnm.android.core.ui.view.textView.PlaceHolderView

/**
 * Single-line search field.
 *
 * @param maxLength Longest accepted query, or null for no limit. Enforced here so a paste cannot
 *   exceed it; callers filtering a list should still cap their own state, since this widget is not
 *   the only way a query can arrive.
 * @param onClear Shows a clear button while [search] is non-empty. Null hides it entirely, which is
 *   the pre-existing behaviour.
 * @param imeAction Keyboard action key. [ImeAction.Search] dismisses the keyboard on submit, which
 *   is what a live-filtering list wants — the results are already on screen behind it.
 * @param containerColor Fill behind the field. Defaults to a surface role that is *distinguishable
 *   from the page*: the previous default was `surface`, which on a Scaffold — whose background is
 *   also a near-identical surface role — drew a field with no visible edge. It reads as floating
 *   text rather than an input, and people do not tap what does not look tappable.
 * @param shape Field outline. A pill reads as "search" at a glance in a way a lightly rounded
 *   rectangle does not.
 */
@Composable
fun AppSearchBar(
    search: String,
    placeHolder: String,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    maxLength: Int? = null,
    onClear: (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Search,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    shape: Shape = RoundedCornerShape(percent = 50),
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = search,
        onValueChange = { value ->
            // Truncate rather than reject: dropping the whole edit loses characters the user
            // already sees, and a paste that is one character too long should still mostly land.
            onSearchChange(if (maxLength != null) value.take(maxLength) else value)
        },

        singleLine = true,

        modifier = modifier
            .fillMaxWidth(),

        textStyle = LocalTextStyle.current.copy(
            fontSize = fontSize,
        ),

        placeholder = {
            PlaceHolderView(placeHolder, fontSize)
        },
        // The whole field, this icon included, already focuses on tap — Material3 routes the
        // container's clicks to the text field. It just did not look like it did, which the
        // container colour above is what actually fixes.
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = stringResource(R.string.search),
                modifier = Modifier.size(20.dp),
            )
        },
        trailingIcon = if (onClear != null && search.isNotEmpty()) {
            {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.search_clear),
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        } else {
            null
        },
        shape = shape,

        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,

            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,

            cursorColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),

        // Text, not Ascii: Ascii restricts the IME and blocks Bengali, Arabic and CJK search terms.
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction,
        ),
        keyboardActions = KeyboardActions(
            onSearch = { keyboardController?.hide() },
            onDone = { keyboardController?.hide() },
        ),
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewAppSearchBar() {
    MaterialTheme {
        // Your AppTheme() if you have one
        var text by remember { mutableStateOf("") }

        AppSearchBar(
            search = text,
            placeHolder = "Search bg text",
            onSearchChange = { text = it },
            maxLength = 48,
            onClear = { text = "" },
            modifier = Modifier.padding(16.dp),
        )
    }
}
