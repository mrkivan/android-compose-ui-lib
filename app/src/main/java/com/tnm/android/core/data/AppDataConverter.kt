package com.tnm.android.core.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class AppDataConverter {
    @TypeConverter
    fun fromStatus(status: TodoTaskStatus): String = status.name

    @TypeConverter
    fun toStatus(status: String): TodoTaskStatus = TodoTaskStatus.valueOf(status)

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime): String = localDateTime.toString()

    @TypeConverter
    fun toLocalDateTime(dateTimeStr: String): LocalDateTime = LocalDateTime.parse(dateTimeStr)

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): String? {
        return localDate?.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun fromLocalTime(localTime: LocalTime?): String? {
        return localTime?.toString()
    }

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }
}