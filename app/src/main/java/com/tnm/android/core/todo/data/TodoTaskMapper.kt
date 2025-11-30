package com.tnm.android.core.todo.data

import com.tnm.android.core.todo.domain.TodoTask

fun TodoTaskEntity.toDomain() = TodoTask(
    id = id,
    taskName = taskName,
    taskDescription = taskDescription,
    selectedDate = selectedDate,
    selectedTime = selectedTime,
    scheduleAt = scheduleAt,
    insertAt = insertAt,
    status = status
)

fun TodoTask.toEntity() = TodoTaskEntity(
    id = id,
    taskName = taskName,
    taskDescription = taskDescription,
    selectedDate = selectedDate,
    selectedTime = selectedTime,
    scheduleAt = scheduleAt,
    insertAt = insertAt,
    status = status
)