package com.tnm.android.core.presentation.widgetShowcase

import com.tnm.android.core.ui.intent.AppUiIntent

/**
 * Implements [AppUiIntent] so this screen follows the same contract as the rest of the app —
 * previously it defined a parallel intent hierarchy the library knew nothing about.
 */
sealed interface WidgetShowcaseIntent : AppUiIntent {
    data object BackPressed : WidgetShowcaseIntent
    data object ConfirmLeave : WidgetShowcaseIntent
    data object ValidateData : WidgetShowcaseIntent
    data object NavigateToTaskList : WidgetShowcaseIntent
}

/** One-shot effects. A single stream keeps ordering intact and leaves no unread channels. */
sealed interface WidgetShowcaseEvent {
    data object NavigateToTaskList : WidgetShowcaseEvent
    data object NavigateBack : WidgetShowcaseEvent
    data object ShowLeaveWarning : WidgetShowcaseEvent
    data class ShowMessage(val message: String) : WidgetShowcaseEvent
}
