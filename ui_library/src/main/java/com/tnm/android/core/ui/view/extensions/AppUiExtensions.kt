package com.tnm.android.core.ui.view.extensions

import androidx.compose.ui.graphics.Color

fun getInactiveColor(
    data: Any?,
    inActiveColor: Color = Color.Gray,
    activeColor: Color = Color.Black
): Color {
    return if (data == null) inActiveColor else activeColor
}