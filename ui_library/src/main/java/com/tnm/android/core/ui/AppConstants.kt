package com.tnm.android.core.ui

import androidx.compose.ui.graphics.Color

object AppConstants {
    const val ASPECT_RATIO = (16f / 9f)

    @Deprecated("Misspelled; use ASPECT_RATIO.", ReplaceWith("ASPECT_RATIO"))
    const val ASPECT_RATION = ASPECT_RATIO
    const val MAX_WIDGET_HEIGHT = 88
    val DARK_MODE_TOPBAR_COLOR = Color(0xFF1E1E1E)
}
