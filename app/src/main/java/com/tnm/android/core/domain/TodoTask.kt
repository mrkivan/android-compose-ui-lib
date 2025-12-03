package com.tnm.android.core.domain

import android.os.Parcelable
import com.tnm.android.core.data.TodoTaskStatus
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Parcelize
data class TodoTask(
    val id: Long = 0L,
    val taskName: String,
    val taskDescription: String,
    val selectedDate: LocalDate,
    val selectedTime: LocalTime,
    val scheduleAt: LocalDateTime,
    val insertAt: LocalDateTime = LocalDateTime.now(),
    val status: TodoTaskStatus = TodoTaskStatus.PENDING,
) : Parcelable