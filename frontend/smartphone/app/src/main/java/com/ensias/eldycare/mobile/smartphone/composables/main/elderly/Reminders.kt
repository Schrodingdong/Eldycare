package com.ensias.eldycare.mobile.smartphone.composables.main.elderly

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ensias.eldycare.mobile.smartphone.R
import com.ensias.eldycare.mobile.smartphone.composables.main.relative.dialogs.MyDatePickerDialog
import com.ensias.eldycare.mobile.smartphone.composables.main.relative.dialogs.MyTimePickerDialog
import com.ensias.eldycare.mobile.smartphone.data.Reminder
import com.ensias.eldycare.mobile.smartphone.data.model.ReminderModel
import com.vanpra.composematerialdialogs.MaterialDialogState
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun RemindersSectionComposable(innerPadding: PaddingValues, remindersList: List<Reminder> = emptyList()){
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ){
        SectionTitle(text = "My\nReminders")
        RemindersList(remindersList)
    }

}
@Composable
fun SectionTitle(text: String){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ){
        text.split('\n').forEach { t ->
            Text(
                text = t,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize
            )
        }
    }

}

@Composable
fun RemindersList(reminders: List<Reminder> = emptyList()) {
    Column (
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 8.dp)
    ){
        if(reminders.isEmpty()){
            // text in the middle of the screen
            Column(
                modifier = Modifier.padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "No reminders Set",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(16.dp))
                Icon(
                    painterResource(id = R.drawable.baseline_calendar_month_24),
                    contentDescription = "No reminders Set",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                )
            }

        } else {
            reminders.forEach { reminder ->
                ReminderItem(reminder)
            }
        }
    }
}




@Composable
fun ReminderItem(reminder: Reminder) {
    val dateText = DateTimeFormatter.ISO_LOCAL_DATE.format(reminder.reminderDate)
    val timeText = DateTimeFormatter.ofPattern("HH:mm").format(reminder.reminderTime)
    OutlinedCard (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(text = dateText, fontWeight = FontWeight.Bold)
                Text(text = timeText, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(text = reminder.description )
            // TODO if there is a localisation or other tha ELDYCARE case insensitive
        }
    }
}

@Composable
fun AddReminderPopup(
    onDismiss: () -> Unit,
    reminderModel: ReminderModel,
    onAddReminder: (Reminder) -> Unit,
    onReminderChange: (ReminderModel) -> Unit,
    formattedDate: String,
    formattedTime: String,
    dateDialogState: MaterialDialogState,
    showDateDialog: (Boolean) -> Unit,
    timeDialogState: MaterialDialogState,
    showTimeDialog: (Boolean) -> Unit,
) {
    val mContext = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { androidx.compose.material3.Text("Add Reminder" ) },
        text = {
            // ========================================================================================
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
//                showDatePickerDialog()
                        showDateDialog(true)
                    }) {
                        Text(text = "Date", color = MaterialTheme.colorScheme.onPrimary)
                    }
                    Text(
                        text = formattedDate,
                        fontWeight = FontWeight.Light,
//                fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                }
                // ========================================================================================
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        showTimeDialog(true)
                    }) {
                        Text(text = "Time", color = MaterialTheme.colorScheme.onPrimary)
                    }
                    Text(
                        text = formattedTime,
                        fontWeight = FontWeight.Light,
//                fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                }
                // ========================================================================================
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    value = reminderModel.description,
                    onValueChange = { onReminderChange(reminderModel.copy(description = it))},
                    label = { Text(text = "Description") },
                )

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val reminder = Reminder(
                        reminderTime = reminderModel.reminderTime,
                        reminderDate = reminderModel.reminderDate,
                        description = reminderModel.description
                    )
                    onAddReminder(reminder)
                    Toast.makeText(mContext, "Reminder added", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
            ) {
                androidx.compose.material3.Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,

                ) {
                androidx.compose.material3.Text("Cancel")
            }
        }
    )
    MyDatePickerDialog(
        dateDialogState = dateDialogState,
        onReminderChange = onReminderChange,
        showDateDialog = showDateDialog,
        showTimeDialog = showTimeDialog,
        reminder = reminderModel
    )
    MyTimePickerDialog(
        timeDialogState = timeDialogState,
        onReminderChange = onReminderChange,
        showTimeDialog = showTimeDialog,
        reminder = reminderModel
    )
}
