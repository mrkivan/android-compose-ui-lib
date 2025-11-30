package com.tnm.android.core.todo.data

import com.tnm.android.core.todo.domain.TodoTaskRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoTaskRepositoryImpl @Inject constructor(
    private val dao: TodoTaskDao
) : TodoTaskRepository {
    override suspend fun addTodoTask(todoTask: TodoTaskEntity) {
        dao.insertTodoTask(todoTask)
    }

    override suspend fun updateTodoTask(todoTask: TodoTaskEntity) {
        dao.updateTodoTask(todoTask)
    }

    override suspend fun getAllTodoTask(): List<TodoTaskEntity> {
        return dao.getAllTask()
    }

    override suspend fun getTodoTaskByStatus(status: TodoTaskStatus): List<TodoTaskEntity> {
        return dao.getTodoTaskByStatus(status)
    }

    override suspend fun deleteTodoTask(todoTask: TodoTaskEntity) {
        dao.deleteTodoTask(todoTask)
    }


}