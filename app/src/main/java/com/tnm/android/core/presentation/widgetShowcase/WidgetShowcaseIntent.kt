package com.tnm.android.core.presentation.widgetShowcase


sealed class WidgetShowcaseIntent {

    data class LoadData(val data: WidgetShowcaseState? = null) : WidgetShowcaseIntent()

    object ShowWarningPopup : WidgetShowcaseIntent()

    object ValidateData : WidgetShowcaseIntent()
    object NavigateToTaskList : WidgetShowcaseIntent()
}

sealed class WidgetShowcaseNavEvent {
    object NavToTaskListScreen : WidgetShowcaseNavEvent()
}