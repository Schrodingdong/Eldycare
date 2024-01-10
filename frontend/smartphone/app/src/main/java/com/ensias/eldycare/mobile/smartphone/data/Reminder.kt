package com.ensias.eldycare.mobile.smartphone.data

import java.time.LocalDate
import java.time.LocalTime

data class Reminder(
    var reminderDate: LocalDate,
    var reminderTime: LocalTime,
    var description: String
)
