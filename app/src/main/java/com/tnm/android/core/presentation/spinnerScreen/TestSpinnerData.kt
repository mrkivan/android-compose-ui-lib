package com.tnm.android.core.presentation.spinnerScreen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestSpinnerData(
    val id: Int,
    val title: String,
    val description: String? = null
) : Parcelable