package com.ensias.eldycare.mobile.smartphone.composables.main.relative.dialogs

import androidx.compose.runtime.Composable
import com.ensias.eldycare.mobile.smartphone.data.model.ReminderModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale

@Composable
fun MyDatePickerDialog(
    dateDialogState: MaterialDialogState,
    onReminderChange: (ReminderModel) -> Unit,
    showDateDialog: (Boolean) -> Unit,
    showTimeDialog: (Boolean) -> Unit,
    reminder: ReminderModel
) {
    MaterialDialog (
        dialogState = dateDialogState,
        buttons = {
            positiveButton("Ok"){
                showDateDialog(false)
                showTimeDialog(true)
            }
            negativeButton("Cancel"){
                showDateDialog(false)
            }
        },
        onCloseRequest = {
            showDateDialog(false)

        }
    ){
        datepicker(
            onDateChange = { onReminderChange(reminder.copy(reminderDate = it)) },
            locale = Locale.ENGLISH,
            allowedDateValidator = { date ->
                date.isAfter(LocalDate.now())
            },
        )
    }

}

@Composable
fun MyTimePickerDialog(
    timeDialogState: MaterialDialogState,
    onReminderChange: (ReminderModel) -> Unit,
    showTimeDialog: (Boolean) -> Unit,
    reminder: ReminderModel
) {
    MaterialDialog (
        dialogState = timeDialogState,
        buttons = {
            positiveButton("Ok"){
                showTimeDialog(false)
            }
            negativeButton("Cancel"){
                showTimeDialog(false)
            }
        },
        onCloseRequest = {
            showTimeDialog(false)
        }
    ){
        timepicker(
            initialTime = LocalTime.now(),
            title = "Select reminder Time",
            onTimeChange = { onReminderChange(reminder.copy(reminderTime = it)) },
            timeRange = LocalTime.MIN..LocalTime.MAX,
        )
    }

}



