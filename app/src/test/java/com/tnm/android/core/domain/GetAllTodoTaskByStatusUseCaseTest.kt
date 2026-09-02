package com.tnm.android.core.domain

import com.tnm.android.core.data.FakeTodoTaskRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class GetAllTodoTaskByStatusUseCaseTest {

    @Test
    fun `emits an empty list when there are no tasks`() = runTest {
        // Regression: this used to throw IllegalStateException("No tasks found"), which the
        // ViewModel turned into an error screen for the perfectly normal "no tasks yet" case.
        val useCase = GetAllTodoTaskByStatusUseCase(FakeTodoTaskRepository())

        assertEquals(emptyList<TodoTask>(), useCase().first())
    }

    @Test
    fun `emits every task when no status is given`() = runTest {
        val repo = FakeTodoTaskRepository(listOf(task(1), task(2, TodoTaskStatus.COMPLETED)))
        val useCase = GetAllTodoTaskByStatusUseCase(repo)

        assertEquals(listOf(1L, 2L), useCase().first().map { it.id })
    }

    @Test
    fun `filters by status when one is given`() = runTest {
        val repo = FakeTodoTaskRepository(
            listOf(task(1), task(2, TodoTaskStatus.COMPLETED), task(3, TodoTaskStatus.COMPLETED)),
        )
        val useCase = GetAllTodoTaskByStatusUseCase(repo)

        val completed = useCase(TodoTaskStatus.COMPLETED).first()

        assertEquals(listOf(2L, 3L), completed.map { it.id })
    }

    @Test
    fun `re-emits after a write`() = runTest {
        // The DAO returning Flow is what makes this true; with the old List-returning DAO the
        // UI never saw an insert.
        val repo = FakeTodoTaskRepository()
        val useCase = GetAllTodoTaskByStatusUseCase(repo)

        repo.addTodoTask(task(1))

        assertEquals(listOf(1L), useCase().first().map { it.id })
    }

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
