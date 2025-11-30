package com.tnm.android.core.ui.view

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

object AppCardDefaults {
    val shape = RoundedCornerShape(8.dp)

    @Composable
    fun elevation() = CardDefaults.cardElevation(defaultElevation = 4.dp)

    @Composable
    fun colors() = CardDefaults.cardColors()
}