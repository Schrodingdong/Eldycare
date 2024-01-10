package com.ensias.eldycare.mobile.smartphone.data.model

import kotlin.random.Random

data class ReminderCalendarEventModel(
    val id: Long = Random(System.currentTimeMillis()).nextLong(),
    val title: String, // Should start with titlePrefix
    val description: String,
    val dtstart: Long,
    val dtend: Long,
    val eventLocation: String,
    val calendarId: Long,
    val eventTimezone: String
){
    companion object{
        const val TITLE_PREFIX = "ELDYCARE - "
    }
}
