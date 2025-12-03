package com.tnm.android.core.presentation.taskListView

import com.tnm.android.core.data.TodoTaskStatus
import com.tnm.android.core.ui.intent.AppUiIntent

sealed class TaskListIntent : AppUiIntent {
    object LoadAllData : TaskListIntent()
    data class LoadDataByStatus(val status: TodoTaskStatus?) : TaskListIntent()
}

