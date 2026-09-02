package com.tnm.android.core.data

import androidx.room.TypeConverter
import com.tnm.android.core.domain.TodoTaskStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class AppDataConverter {
    @TypeConverter
    fun fromStatus(status: TodoTaskStatus): String = status.name

    // Rows written by an older build can hold a value this enum no longer declares; valueOf()
    // would throw while simply reading the table.
    @TypeConverter
    fun toStatus(status: String): TodoTaskStatus =
        TodoTaskStatus.entries.firstOrNull { it.name == status } ?: TodoTaskStatus.PENDING

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime): String = localDateTime.toString()

    @TypeConverter
    fun toLocalDateTime(dateTimeStr: String): LocalDateTime = LocalDateTime.parse(dateTimeStr)

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): String? = localDate?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromLocalTime(localTime: LocalTime?): String? = localTime?.toString()

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? = value?.let { LocalTime.parse(it) }
}
