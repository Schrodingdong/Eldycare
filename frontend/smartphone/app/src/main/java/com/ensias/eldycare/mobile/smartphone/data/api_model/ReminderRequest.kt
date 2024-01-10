package com.ensias.eldycare.mobile.smartphone.data.api_model

data class ReminderRequest(
    var elderEmail: String,
    var relativeEmail: String, // TODO : to delete
    var reminderDate: String,
    var reminderTime: String,
    var description: String
)
