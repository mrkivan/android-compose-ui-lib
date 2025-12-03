package com.tnm.android.core.domain

import com.tnm.android.core.data.TodoTaskEntity
import com.tnm.android.core.data.TodoTaskStatus

interface TodoTaskRepository {
    suspend fun addTodoTask(todoTask: TodoTaskEntity)
    suspend fun updateTodoTask(todoTask: TodoTaskEntity)
    suspend fun getAllTodoTask(): List<TodoTaskEntity>
    suspend fun getTodoTaskByStatus(status: TodoTaskStatus): List<TodoTaskEntity>
    suspend fun deleteTodoTask(todoTask: TodoTaskEntity)
}