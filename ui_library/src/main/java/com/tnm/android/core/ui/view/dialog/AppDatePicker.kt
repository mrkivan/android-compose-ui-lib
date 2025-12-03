package com.tnm.android.core.ui.view.dialog

import android.app.DatePickerDialog
import android.content.Context
import java.time.LocalDate
import java.util.Calendar

fun showAppDatePicker(
    validateDate: (LocalDate) -> Boolean,
    onDateSelected: (LocalDate) -> Unit,
    context: Context
) {
    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, day ->
            val picked = LocalDate.of(year, month + 1, day)
            if (validateDate(picked)) {
                onDateSelected(picked)
            }
        },
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
    datePicker.show()
}