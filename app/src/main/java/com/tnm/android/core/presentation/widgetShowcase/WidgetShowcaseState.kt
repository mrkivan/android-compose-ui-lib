package com.tnm.android.core.presentation.widgetShowcase

import android.os.Parcelable
import com.tnm.android.core.domain.TodoTask
import kotlinx.parcelize.Parcelize

@Parcelize
data class WidgetShowcaseState(
    val entity: TodoTask? = null,
    val isEdit: Boolean = false,
    val isView: Boolean = false
) : Parcelable