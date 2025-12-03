package com.tnm.android.core.domain

import com.tnm.android.core.data.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteTodoTaskUseCase @Inject constructor(
    private val repo: TodoTaskRepository
) {
    operator fun invoke(data: TodoTask): Flow<Unit> = flow {
        repo.deleteTodoTask(data.toEntity())
        emit(Unit)
    }
}