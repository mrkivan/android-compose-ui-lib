package com.tnm.android.core.todo.domain

import com.tnm.android.core.todo.data.TodoTaskEntity
import com.tnm.android.core.todo.data.TodoTaskStatus

interface TodoTaskRepository {
    suspend fun addTodoTask(todoTask: TodoTaskEntity)
    suspend fun updateTodoTask(todoTask: TodoTaskEntity)
    suspend fun getAllTodoTask(): List<TodoTaskEntity>
    suspend fun getTodoTaskByStatus(status: TodoTaskStatus): List<TodoTaskEntity>
    suspend fun deleteTodoTask(todoTask: TodoTaskEntity)
}