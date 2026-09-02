package com.tnm.android.core.presentation.taskListView

import com.tnm.android.core.MainDispatcherRule
import com.tnm.android.core.data.FakeTodoTaskRepository
import com.tnm.android.core.domain.GetAllTodoTaskByStatusUseCase
import com.tnm.android.core.domain.TodoTask
import com.tnm.android.core.domain.TodoTaskStatus
import com.tnm.android.core.ui.state.AppUiState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class TaskListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `starts in loading`() {
        val viewModel = viewModel(FakeTodoTaskRepository())

        assertTrue(viewModel.state.value is AppUiState.Loading)
    }

    @Test
    fun `emits success with the tasks on LoadAllData`() = runTest {
        val repo = FakeTodoTaskRepository(listOf(task(1), task(2)))
        val viewModel = viewModel(repo)

        viewModel.handleIntent(TaskListIntent.LoadAllData)

        val state = viewModel.state.value
        assertTrue(state is AppUiState.Success)
        assertEquals(listOf(1L, 2L), (state as AppUiState.Success).data.map { it.id })
    }

    @Test
    fun `an empty result is success not error`() = runTest {
        // The screen should show an empty list, not an error with a retry button.
        val viewModel = viewModel(FakeTodoTaskRepository())

        viewModel.handleIntent(TaskListIntent.LoadAllData)

        val state = viewModel.state.value
        assertTrue(state is AppUiState.Success)
        assertEquals(emptyList<TodoTask>(), (state as AppUiState.Success).data)
    }

    @Test
    fun `emits error when the source fails`() = runTest {
        val repo = FakeTodoTaskRepository(listOf(task(1)))
        repo.failWith = IllegalStateException("database unavailable")
        val viewModel = viewModel(repo)

        viewModel.handleIntent(TaskListIntent.LoadAllData)

        val state = viewModel.state.value
        assertTrue(state is AppUiState.Error)
        assertEquals("database unavailable", (state as AppUiState.Error).message)
    }

    @Test
    fun `filters by status on LoadDataByStatus`() = runTest {
        val repo = FakeTodoTaskRepository(
            listOf(task(1), task(2, TodoTaskStatus.COMPLETED)),
        )
        val viewModel = viewModel(repo)

        viewModel.handleIntent(TaskListIntent.LoadDataByStatus(TodoTaskStatus.COMPLETED))

        val state = viewModel.state.value
        assertTrue(state is AppUiState.Success)
        assertEquals(listOf(2L), (state as AppUiState.Success).data.map { it.id })
    }

    private fun viewModel(repo: FakeTodoTaskRepository) = TaskListViewModel(GetAllTodoTaskByStatusUseCase(repo))

    private fun task(id: Long, status: TodoTaskStatus = TodoTaskStatus.PENDING) = TodoTask(
        id = id,
        taskName = "Task $id",
        taskDescription = "Description $id",
        selectedDate = LocalDate.of(2026, 1, 1),
        selectedTime = LocalTime.of(9, 0),
        scheduleAt = LocalDateTime.of(2026, 1, 1, 9, 0),
        insertAt = LocalDateTime.of(2026, 1, 1, 8, 0),
        status = status,
    )
}
