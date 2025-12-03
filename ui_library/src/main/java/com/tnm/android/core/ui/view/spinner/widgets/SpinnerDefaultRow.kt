package com.tnm.android.core.ui.view.spinner.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tnm.android.core.ui.view.shape.SpacerHeightMedium
import com.tnm.android.core.ui.view.textView.TvBodyMedium
import com.tnm.android.core.ui.view.textView.TvTitleLarge

@Composable
fun SpinnerDefaultRow(
    label: String,
    isSelected: Boolean,
    isMultiSelectEnable: Boolean,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TvTitleLarge(text = label, modifier = Modifier.weight(1f))

            if (isMultiSelectEnable) {
                Checkbox(checked = isSelected, onCheckedChange = null)
            } else if (isSelected) {
                Icon(Icons.Filled.Check, contentDescription = null)
            }
        }
        if (!description.isNullOrEmpty()) {
            SpacerHeightMedium()
            TvBodyMedium(text = description, modifier = Modifier.fillMaxWidth())
        }

    }
}
