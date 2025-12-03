package com.tnm.android.core.presentation.taskListView

import com.tnm.android.core.data.TodoTaskStatus
import com.tnm.android.core.domain.GetAllTodoTaskByStatusUseCase
import com.tnm.android.core.domain.TodoTask
import com.tnm.android.core.domain.UpdateTodoTaskUseCase
import com.tnm.android.core.ui.intent.AppUiIntent
import com.tnm.android.core.ui.viewmodel.BaseDataLoadingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getAllTodoTaskByStatusUseCase: GetAllTodoTaskByStatusUseCase,
    private val updateTodoTaskUseCase: UpdateTodoTaskUseCase,
) : BaseDataLoadingViewModel<List<TodoTask>>() {


    override fun dataFlow(param: Any?): Flow<List<TodoTask>> {
        val status = param as? TodoTaskStatus
        return getAllTodoTaskByStatusUseCase(status)
    }


    override fun handleIntent(intent: AppUiIntent) {
        when (intent) {
            is TaskListIntent.LoadAllData -> {
                //fetchData()
                //TODO remove this, only for testing
                val mockList = generateMockTasks(30)
                setSuccess(mockList)
            }

            is TaskListIntent.LoadDataByStatus -> {
                fetchData(intent.status)
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