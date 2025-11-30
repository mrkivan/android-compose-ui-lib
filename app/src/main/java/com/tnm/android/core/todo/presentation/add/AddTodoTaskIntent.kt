package com.tnm.android.core.todo.presentation.add


sealed class AddTodoTaskIntent {

    data class LoadData(val data: AddTodoTaskState? = null) : AddTodoTaskIntent()

    object ShowWarningPopup : AddTodoTaskIntent()

    object ValidateData : AddTodoTaskIntent()
}