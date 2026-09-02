package com.tnm.android.core.presentation.taskListView

import com.tnm.android.core.domain.GetAllTodoTaskByStatusUseCase
import com.tnm.android.core.domain.TodoTask
import com.tnm.android.core.domain.TodoTaskStatus
import com.tnm.android.core.ui.intent.AppUiIntent
import com.tnm.android.core.ui.viewmodel.BaseDataLoadingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(private val getAllTodoTaskByStatusUseCase: GetAllTodoTaskByStatusUseCase) :
    BaseDataLoadingViewModel<List<TodoTask>>() {

    override fun dataFlow(param: Any?): Flow<List<TodoTask>> {
        val status = param as? TodoTaskStatus
        return getAllTodoTaskByStatusUseCase(status)
    }

    override fun handleIntent(intent: AppUiIntent) {
        when (intent) {
            is TaskListIntent.LoadAllData -> fetchData()
            is TaskListIntent.LoadDataByStatus -> fetchData(intent.status)
        }
    }
}
