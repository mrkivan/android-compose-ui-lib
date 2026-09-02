package com.tnm.android.core.domain

import kotlinx.coroutines.flow.Flow

/**
 * Domain-facing contract. It speaks [TodoTask] only — the Room entity is a data-layer detail and
 * must not appear here, or every schema change ripples into the domain and UI.
 */
interface TodoTaskRepository {
    suspend fun addTodoTask(todoTask: TodoTask)
    suspend fun updateTodoTask(todoTask: TodoTask)
    fun observeAllTodoTasks(): Flow<List<TodoTask>>
    fun observeTodoTasksByStatus(status: TodoTaskStatus): Flow<List<TodoTask>>
    suspend fun deleteTodoTask(todoTask: TodoTask)
}
