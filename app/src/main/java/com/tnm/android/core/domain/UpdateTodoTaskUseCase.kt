package com.tnm.android.core.domain

import com.tnm.android.core.data.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateTodoTaskUseCase @Inject constructor(
    private val repo: TodoTaskRepository
) {
    operator fun invoke(data: TodoTask): Flow<Unit> = flow {
        repo.updateTodoTask(data.toEntity())
        emit(Unit)
    }
}