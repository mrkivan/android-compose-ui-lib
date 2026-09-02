package com.tnm.android.core.data

import com.tnm.android.core.domain.TodoTask
import com.tnm.android.core.domain.TodoTaskRepository
import com.tnm.android.core.domain.TodoTaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * A fake, not a mock: it survives interface changes with one edit and reads like the real thing,
 * where a mock would encode call sequences that break on every refactor.
 */
class FakeTodoTaskRepository(initialTasks: List<TodoTask> = emptyList()) : TodoTaskRepository {

    private val tasks = MutableStateFlow(initialTasks)

    /** Set to make the observed flows fail, for exercising error paths. */
    var failWith: Throwable? = null

    override suspend fun addTodoTask(todoTask: TodoTask) {
        tasks.value = tasks.value + todoTask
    }

    override suspend fun updateTodoTask(todoTask: TodoTask) {
        tasks.value = tasks.value.map { if (it.id == todoTask.id) todoTask else it }
    }

    override fun observeAllTodoTasks(): Flow<List<TodoTask>> = observe { true }

    override fun observeTodoTasksByStatus(status: TodoTaskStatus): Flow<List<TodoTask>> =
        observe { it.status == status }

    override suspend fun deleteTodoTask(todoTask: TodoTask) {
        tasks.value = tasks.value.filterNot { it.id == todoTask.id }
    }

    private fun observe(predicate: (TodoTask) -> Boolean): Flow<List<TodoTask>> = tasks.map { list ->
        failWith?.let { throw it }
        list.filter(predicate)
    }
}
