package com.tnm.android.core.todo.presentation.dashboard

import androidx.lifecycle.viewModelScope
import com.tnm.android.core.todo.data.TodoTaskStatus
import com.tnm.android.core.todo.domain.GetAllTodoTaskByStatusUseCase
import com.tnm.android.core.todo.domain.TodoTask
import com.tnm.android.core.todo.domain.UpdateTodoTaskUseCase
import com.tnm.android.core.ui.intent.AppUiIntent
import com.tnm.android.core.ui.viewmodel.BaseDataLoadingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllTodoTaskByStatusUseCase: GetAllTodoTaskByStatusUseCase,
    private val updateTodoTaskUseCase: UpdateTodoTaskUseCase,
) : BaseDataLoadingViewModel<List<TodoTask>>() {
    private val _navigationEvents = MutableSharedFlow<DashboardNavEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    override fun dataFlow(param: Any?): Flow<List<TodoTask>> {
        val status = param as? TodoTaskStatus
        return getAllTodoTaskByStatusUseCase(status)
    }


    override fun handleIntent(intent: AppUiIntent) {
        when (intent) {
            is DashboardIntent.LoadAllData -> {
                //fetchData()
                //TODO remove this, only for testing
                val mockList = generateMockTasks(30)
                setSuccess(mockList)
            }

            is DashboardIntent.LoadDataByStatus -> {
                fetchData(intent.status)
            }

            is DashboardIntent.NavigateToAddTodoTask -> {
                viewModelScope.launch {
                    _navigationEvents.emit(DashboardNavEvent.NavToAddTodoTask)
                }
            }
        }
    }

    //TODO remove this, only for testing
    fun generateMockTasks(count: Int = 10): List<TodoTask> {
        return List(count) { index ->
            val date = LocalDate.now().plusDays((0..5).random().toLong())
            val time = LocalTime.of((8..20).random(), listOf(0, 15, 30, 45).random())
            val scheduleAt = LocalDateTime.of(date, time)

            TodoTask(
                id = index + 1L,
                taskName = "Task ${index + 1}",
                taskDescription = "Description for task ${index + 1}",
                selectedDate = date,
                selectedTime = time,
                scheduleAt = scheduleAt,
                insertAt = LocalDateTime.now().minusMinutes((0..500).random().toLong()),
                status = TodoTaskStatus.entries.toTypedArray().random()
            )
        }
    }


}