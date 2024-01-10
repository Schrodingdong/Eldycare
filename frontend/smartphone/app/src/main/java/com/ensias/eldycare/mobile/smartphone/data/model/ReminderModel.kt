package com.ensias.eldycare.mobile.smartphone.data.model

import java.time.LocalDate
import java.time.LocalTime

data class ReminderModel(
    var elderEmail: String,
    var relativeEmail: String, // TODO : to delete
    var reminderDate: LocalDate,
    var reminderTime: LocalTime,
    var description: String,
)
