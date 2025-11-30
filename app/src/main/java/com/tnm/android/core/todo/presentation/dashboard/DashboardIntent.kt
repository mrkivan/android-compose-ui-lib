package com.tnm.android.core.todo.presentation.dashboard

import com.tnm.android.core.todo.data.TodoTaskStatus
import com.tnm.android.core.ui.intent.AppUiIntent

sealed class DashboardIntent : AppUiIntent {
    object LoadAllData : DashboardIntent()
    data class LoadDataByStatus(val status: TodoTaskStatus?) : DashboardIntent()

    object NavigateToAddTodoTask : DashboardIntent()
}

sealed class DashboardNavEvent {
    object NavToAddTodoTask : DashboardNavEvent()
}