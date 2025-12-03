package com.tnm.android.core.data

import com.tnm.android.core.domain.TodoTask

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