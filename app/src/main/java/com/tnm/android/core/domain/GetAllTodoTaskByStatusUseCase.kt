package com.tnm.android.core.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTodoTaskByStatusUseCase @Inject constructor(private val repo: TodoTaskRepository) {
    /**
     * Emits the current tasks and every subsequent change.
     *
     * An empty result is a legitimate outcome, not a failure: throwing here surfaced an error
     * screen with a retry button whenever the user simply had no tasks yet. "Nothing to show"
     * belongs to the UI as an empty state.
     */
    operator fun invoke(status: TodoTaskStatus? = null): Flow<List<TodoTask>> =
        if (status == null) repo.observeAllTodoTasks() else repo.observeTodoTasksByStatus(status)
}
