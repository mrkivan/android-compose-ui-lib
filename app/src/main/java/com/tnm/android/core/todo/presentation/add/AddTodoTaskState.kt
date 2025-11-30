package com.tnm.android.core.todo.presentation.add

import android.os.Parcelable
import com.tnm.android.core.todo.domain.TodoTask
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddTodoTaskState(
    val entity: TodoTask? = null,
    val isEdit: Boolean = false,
    val isView: Boolean = false
) : Parcelable