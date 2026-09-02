package com.tnm.android.core.data

import com.tnm.android.core.domain.TodoTask
import com.tnm.android.core.domain.TodoTaskStatus
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class TodoTaskMapperTest {

    private val domain = TodoTask(
        id = 7,
        taskName = "Write tests",
        taskDescription = "Cover the mapping boundary",
        selectedDate = LocalDate.of(2026, 5, 1),
        selectedTime = LocalTime.of(11, 30),
        scheduleAt = LocalDateTime.of(2026, 5, 1, 11, 30),
        insertAt = LocalDateTime.of(2026, 4, 30, 9, 0),
        status = TodoTaskStatus.COMPLETED,
    )

    @Test
    fun `domain to entity and back preserves every field`() {
        assertEquals(domain, domain.toEntity().toDomain())
    }

    @Test
    fun `entity maps field by field`() {
        val entity = domain.toEntity()

        assertEquals(domain.id, entity.id)
        assertEquals(domain.taskName, entity.taskName)
        assertEquals(domain.taskDescription, entity.taskDescription)
        assertEquals(domain.selectedDate, entity.selectedDate)
        assertEquals(domain.selectedTime, entity.selectedTime)
        assertEquals(domain.scheduleAt, entity.scheduleAt)
        assertEquals(domain.insertAt, entity.insertAt)
        assertEquals(domain.status, entity.status)
    }
}
