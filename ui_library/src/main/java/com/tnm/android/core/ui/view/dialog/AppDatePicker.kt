package com.tnm.android.core.ui.view.dialog

import android.app.DatePickerDialog
import android.content.Context
import java.time.LocalDate

/**
 * Shows the platform date picker.
 *
 * [onInvalidDate] is called when [validateDate] rejects the pick. Previously the selection was
 * dropped in silence, so the user tapped OK and nothing happened with no explanation — pass a
 * handler to show a message, or leave it out to keep the old behaviour deliberately.
 *
 * [selectedDate] seeds the dialog, matching [showAppTimePicker], which already took an initial value.
 */
fun showAppDatePicker(
    validateDate: (LocalDate) -> Boolean,
    onDateSelected: (LocalDate) -> Unit,
    context: Context,
    selectedDate: LocalDate? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    onInvalidDate: (LocalDate) -> Unit = {},
) {
    // Clamp into range first: DatePickerDialog throws if the initial date sits outside
    // minDate..maxDate.
    val initial = (selectedDate ?: LocalDate.now())
        .let { if (minDate != null && it.isBefore(minDate)) minDate else it }
        .let { if (maxDate != null && it.isAfter(maxDate)) maxDate else it }

    val datePicker = DatePickerDialog(
        context,
        { _, year, month, day ->
            // DatePickerDialog months are 0-based; LocalDate months are 1-based.
            val picked = LocalDate.of(year, month + 1, day)
            if (validateDate(picked)) {
                onDateSelected(picked)
            } else {
                onInvalidDate(picked)
            }
        },
        initial.year,
        initial.monthValue - 1,
        initial.dayOfMonth,
    )

    // Bounds keep invalid dates unpickable rather than rejecting them after the fact.
    minDate?.let { datePicker.datePicker.minDate = it.toEpochMillis() }
    maxDate?.let { datePicker.datePicker.maxDate = it.toEpochMillis() }

    datePicker.show()
}

private fun LocalDate.toEpochMillis(): Long = atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
