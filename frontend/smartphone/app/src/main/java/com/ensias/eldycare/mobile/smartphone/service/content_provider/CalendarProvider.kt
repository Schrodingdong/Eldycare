package com.ensias.eldycare.mobile.smartphone.service.content_provider

import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import com.ensias.eldycare.mobile.smartphone.data.model.ReminderCalendarEventModel

class CalendarProvider(val context: Context) {
    fun writeToCalendar(reminderCalendarEventModel: ReminderCalendarEventModel){
        val values = ContentValues().apply {
            put(CalendarContract.Events.TITLE, reminderCalendarEventModel.title)
            put(CalendarContract.Events.DESCRIPTION, reminderCalendarEventModel.description)
            put(CalendarContract.Events.DTSTART, reminderCalendarEventModel.dtstart)
            put(CalendarContract.Events.DTEND, reminderCalendarEventModel.dtend)
            put(CalendarContract.Events.EVENT_LOCATION, reminderCalendarEventModel.eventLocation)
            put(CalendarContract.Events.CALENDAR_ID, reminderCalendarEventModel.calendarId)
            put(CalendarContract.Events.EVENT_TIMEZONE, reminderCalendarEventModel.eventTimezone)
        }
        val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        Log.d("CalendarProvider", "Event added to calendar : $uri")
    }
    fun readFromCalendar() : List<ReminderCalendarEventModel>{
        val eldycareReminders = mutableListOf<ReminderCalendarEventModel>()
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.CALENDAR_ID,
            CalendarContract.Events.EVENT_TIMEZONE,
        )
        // select 10 or less Recent titles that contains TITLE_PREFIX and future events and not deleted
        val selectionClause = "${CalendarContract.Events.TITLE} LIKE ? AND ${CalendarContract.Events.DTSTART} > ? AND ${CalendarContract.Events.DELETED} = 0"
        val selectionArgs = arrayOf("%${ReminderCalendarEventModel.TITLE_PREFIX}%", System.currentTimeMillis().toString())
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC LIMIT 10"
        context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            selectionClause,
            selectionArgs,
            sortOrder,
        )?.use {cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(CalendarContract.Events._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE)
            val descriptionColumn = cursor.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION)
            val dtstartColumn = cursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)
            val dtendColumn = cursor.getColumnIndexOrThrow(CalendarContract.Events.DTEND)
            val eventLocationColumn = cursor.getColumnIndexOrThrow(CalendarContract.Events.EVENT_LOCATION)
            val calendarIdColumn = cursor.getColumnIndexOrThrow(CalendarContract.Events.CALENDAR_ID)
            val eventTimezoneColumn = cursor.getColumnIndexOrThrow(CalendarContract.Events.EVENT_TIMEZONE)

            while(cursor.moveToNext()){
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val description = cursor.getString(descriptionColumn)
                val dtstart = cursor.getLong(dtstartColumn)
                val dtend = cursor.getLong(dtendColumn)
                val eventLocation = cursor.getString(eventLocationColumn)
                val calendarId = cursor.getLong(calendarIdColumn)
                val eventTimezone = cursor.getString(eventTimezoneColumn)

                val reminderCalendarEventModel = ReminderCalendarEventModel(
                    id,
                    title,
                    description,
                    dtstart,
                    dtend,
                    eventLocation,
                    calendarId,
                    eventTimezone
                )
                eldycareReminders.add(reminderCalendarEventModel)
                Log.d("CalendarProvider", "ReminderCalendarEventModel : $reminderCalendarEventModel")
            }

        }
        return eldycareReminders.toList()
    }
}
