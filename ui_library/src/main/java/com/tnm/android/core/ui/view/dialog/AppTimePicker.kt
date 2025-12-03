package com.tnm.android.core.ui.view.dialog

import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat
import java.time.LocalTime

fun showAppTimePicker(
    validateTime: (LocalTime) -> Boolean,
    onSelectedTime: (LocalTime) -> Unit,
    selectedTime: LocalTime?,
    context: Context
) {
    val is24Hour = DateFormat.is24HourFormat(context)
    val initialTime = selectedTime ?: LocalTime.now()

    val timePicker = TimePickerDialog(
        context,
        { _, hour, minute ->
            val pickedTime = LocalTime.of(hour, minute)
            if (validateTime(pickedTime)) {
                onSelectedTime(pickedTime)
            }
        },
        initialTime.hour,
        initialTime.minute,
        is24Hour
    )

    timePicker.show()
}