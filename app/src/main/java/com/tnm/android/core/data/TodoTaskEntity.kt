package com.tnm.android.core.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity(
    tableName = "table_todo_task",
)
data class TodoTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val taskName: String,
    val taskDescription: String,
    val selectedDate: LocalDate,
    val selectedTime: LocalTime,
    val scheduleAt: LocalDateTime,
    val insertAt: LocalDateTime = LocalDateTime.now(),
    val status: TodoTaskStatus = TodoTaskStatus.PENDING,
)