package com.tnm.android.core.data

import com.tnm.android.core.domain.TodoTaskStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class AppDataConverterTest {

    private val converter = AppDataConverter()

    @Test
    fun `status round trips`() {
        TodoTaskStatus.entries.forEach { status ->
            assertEquals(status, converter.toStatus(converter.fromStatus(status)))
        }
    }

    @Test
    fun `unknown status falls back instead of throwing`() {
        // A row written by an older build must not crash a plain read; valueOf() would throw.
        assertEquals(TodoTaskStatus.PENDING, converter.toStatus("ARCHIVED_IN_SOME_OLD_BUILD"))
    }

    @Test
    fun `date time round trips`() {
        val value = LocalDateTime.of(2026, 3, 14, 15, 9, 26)

        assertEquals(value, converter.toLocalDateTime(converter.fromLocalDateTime(value)))
    }

    @Test
    fun `date and time round trip`() {
        val date = LocalDate.of(2026, 3, 14)
        val time = LocalTime.of(15, 9)

        assertEquals(date, converter.toLocalDate(converter.fromLocalDate(date)))
        assertEquals(time, converter.toLocalTime(converter.fromLocalTime(time)))
    }

    @Test
    fun `nulls survive the round trip`() {
        assertNull(converter.fromLocalDate(null))
        assertNull(converter.toLocalDate(null))
        assertNull(converter.fromLocalTime(null))
        assertNull(converter.toLocalTime(null))
    }
}
