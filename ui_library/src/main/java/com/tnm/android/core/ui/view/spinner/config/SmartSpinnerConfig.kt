package com.tnm.android.core.ui.view.spinner.config

import android.os.Parcelable
import com.tnm.android.core.ui.AppConstants.MAX_WIDGET_HEIGHT
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * @property widgetTitle Label of the trigger row and title of the picker.
 * @property widgetPlaceholder Text shown on the trigger row while nothing is selected. Blank falls back to [widgetTitle].
 * @property gridColumns Render the picker as a grid with this many columns; null renders a list.
 */
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
    @Deprecated("Use gridColumns; a Pair<Boolean, Int> says nothing about what its halves mean.")
    val isGrid: Pair<Boolean, Int> = Pair(false, 0),
    @IgnoredOnParcel
    val rowLabel: (T) -> String = { it.toString() },
    @IgnoredOnParcel
    val onResult: (Set<T>) -> Unit = {},
    val gridColumns: Int? = null,
) : Parcelable {
    /** Effective column count: [gridColumns] wins, then the deprecated [isGrid] pair, else null (list). */
    @Suppress("DEPRECATION")
    val effectiveGridColumns: Int?
        get() = gridColumns?.coerceAtLeast(1) ?: isGrid.takeIf { it.first }?.second?.coerceAtLeast(1)
}
