package com.tnm.android.core.data

import com.tnm.android.core.domain.TodoTask
import com.tnm.android.core.domain.TodoTaskRepository
import com.tnm.android.core.domain.TodoTaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The mapping boundary: entities stay inside this class, domain models leave it.
 */
@Singleton
class TodoTaskRepositoryImpl @Inject constructor(private val dao: TodoTaskDao) : TodoTaskRepository {

    override suspend fun addTodoTask(todoTask: TodoTask) {
        dao.insertTodoTask(todoTask.toEntity())
    }

    override suspend fun updateTodoTask(todoTask: TodoTask) {
        dao.updateTodoTask(todoTask.toEntity())
    }

    override fun observeAllTodoTasks(): Flow<List<TodoTask>> =
        dao.observeAllTasks().map { entities -> entities.map { it.toDomain() } }

    override fun observeTodoTasksByStatus(status: TodoTaskStatus): Flow<List<TodoTask>> =
        dao.observeTasksByStatus(status).map { entities -> entities.map { it.toDomain() } }

    override suspend fun deleteTodoTask(todoTask: TodoTask) {
        dao.deleteTodoTask(todoTask.toEntity())
    }
}
