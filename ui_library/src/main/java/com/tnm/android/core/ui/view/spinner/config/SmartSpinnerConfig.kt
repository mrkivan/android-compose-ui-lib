package com.tnm.android.core.ui.view.spinner.config

import android.os.Parcelable
import com.tnm.android.core.ui.AppConstants.MAX_WIDGET_HEIGHT
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class SmartSpinnerConfig<T>(
    val widgetTitle: String,
    val widgetPlaceholder: String,
    val searchable: Boolean = false,
    val searchPlaceHolder: String? = null,
    val multiSelectEnable: Boolean = false,
    val spinnerType: SpinnerDisplayType = SpinnerDisplayType.Dialog,
    val designFlat: Boolean = false,
    val maxHeight: Int = MAX_WIDGET_HEIGHT,
    @IgnoredOnParcel
    val rowLabel: (T) -> String = { it.toString() },
    @IgnoredOnParcel
    val onResult: (Set<T>) -> Unit = {},
) : Parcelable