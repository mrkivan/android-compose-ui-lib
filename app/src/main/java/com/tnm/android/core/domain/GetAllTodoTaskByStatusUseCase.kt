package com.tnm.android.core.domain

import com.tnm.android.core.data.TodoTaskStatus
import com.tnm.android.core.data.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllTodoTaskByStatusUseCase @Inject constructor(
    private val repo: TodoTaskRepository
) {
    operator fun invoke(status: TodoTaskStatus? = null): Flow<List<TodoTask>> = flow {
        val result = if (status == null) {
            repo.getAllTodoTask()
        } else {
            repo.getTodoTaskByStatus(status)
        }
        if (result.isEmpty()) {
            throw IllegalStateException("No tasks found")
        }
        emit(result)
    }.map { list ->
        list.map { it.toDomain() }
    }

}