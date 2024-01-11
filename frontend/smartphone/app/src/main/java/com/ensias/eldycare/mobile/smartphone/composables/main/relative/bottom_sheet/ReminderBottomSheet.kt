package com.ensias.eldycare.mobile.smartphone.composables.main.relative.bottom_sheet

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.composables.main.relative.dialogs.MyDatePickerDialog
import com.ensias.eldycare.mobile.smartphone.composables.main.relative.dialogs.MyTimePickerDialog
import com.ensias.eldycare.mobile.smartphone.data.Connection
import com.ensias.eldycare.mobile.smartphone.data.api_model.ReminderRequest
import com.ensias.eldycare.mobile.smartphone.data.api_model.ReminderResponse
import com.ensias.eldycare.mobile.smartphone.data.model.ReminderModel
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// ========================================================================================
// Bottom Sheet components
// ========================================================================================
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun BottomSheetContent(connection: Connection, showDatePickerDialog: () -> Unit, onDismissBottomSheet: (String) -> Unit){
    var reminder by remember {
        mutableStateOf(ReminderModel(
            reminderDate = LocalDate.now(),
            reminderTime = LocalTime.now(),
            description = "",
            elderEmail = connection.email,
            relativeEmail = "")
        )
    }
    val formattedDate by remember{
        derivedStateOf{
            DateTimeFormatter
                .ISO_LOCAL_DATE
                .format(reminder.reminderDate)
        }
    }
    val formattedTime by remember{
        derivedStateOf{
            DateTimeFormatter
                .ofPattern("HH:mm")
                .format(reminder.reminderTime)
        }
    }
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        BottomSheetConnectionInfo(connection = connection)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 32.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            thickness = 1.dp
        )
        BottomSheetAddReminder(
            showDatePickerDialog = showDatePickerDialog,
            reminder = reminder,
            onReminderChange = { reminder = it },
            formattedDate = formattedDate,
            formattedTime = formattedTime,
            dateDialogState = dateDialogState,
            showDateDialog = { if(it == true) dateDialogState.show() else dateDialogState.hide() },
            timeDialogState = timeDialogState,
            showTimeDialog = { if(it == true) timeDialogState.show() else timeDialogState.hide() },
            onDismissBottomSheet = onDismissBottomSheet
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetAddReminder(
    showDatePickerDialog: () -> Unit,
    reminder: ReminderModel,
    onReminderChange: (ReminderModel) -> Unit,
    formattedDate: String,
    formattedTime: String,
    dateDialogState: MaterialDialogState,
    showDateDialog: (Boolean) -> Unit,
    timeDialogState: MaterialDialogState,
    showTimeDialog: (Boolean) -> Unit,
    onDismissBottomSheet: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 56.dp,),
    ) {
        Text(
            text = "Add Reminder",
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineLarge.fontSize
        )
        // ========================================================================================
        Spacer(modifier = Modifier.height(16.dp))
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
            value = reminder.description,
            onValueChange = { onReminderChange(reminder.copy(description = it))},
            label = { Text(text = "Description") },
        )
        // ========================================================================================
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Button(onClick = {
                // send reminder to server
                GlobalScope.launch {
                    var res: retrofit2.Response<ReminderResponse>
                    val reminderRequest = ReminderRequest(
                        reminderDate = formattedDate,
                        reminderTime = formattedTime,
                        description = reminder.description,
                        elderEmail = reminder.elderEmail,
                        relativeEmail = reminder.relativeEmail
                    )
                    // sent to the server
                    runBlocking{
                        res = ApiClient().authApi.sendReminder(reminderRequest)
                        Log.d("BottomSheetContent", "reminder : " + reminderRequest.toString())
                    }
                    // w8 for response for 10 seconds
                    var counter = 0
                    val THRESHOLD = 10
                    while(!res.isSuccessful && counter < THRESHOLD){
                        delay(1000);
                        Log.d("BottomSheetContent", "is succesfull : " +  res.isSuccessful)
                        counter++
                    }
                    // action to partake
                    if(counter == THRESHOLD && !res.isSuccessful){
                        Log.d("BottomSheetContent", "is succesfull : " +  res.isSuccessful)
                        launch(Dispatchers.Main) {// FINALLLYYYYYYYYYYYYYYYYYYYYYYYY
                            onDismissBottomSheet("Failed to send reminder")
                        }
                    } else {
                        Log.d("BottomSheetContent", "is succesfull : " +  res.isSuccessful)
                        launch(Dispatchers.Main) {
                            onDismissBottomSheet("Reminder Sent !")
                        }
                    }
                }
            }) {
                Text(text = "Add", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
    MyDatePickerDialog(
        dateDialogState = dateDialogState,
        onReminderChange = onReminderChange,
        showDateDialog = showDateDialog,
        showTimeDialog = showTimeDialog,
        reminder = reminder
    )
    MyTimePickerDialog(
        timeDialogState = timeDialogState,
        onReminderChange = onReminderChange,
        showTimeDialog = showTimeDialog,
        reminder = reminder
    )
}

@Composable
fun BottomSheetConnectionInfo(connection: Connection){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 32.dp)
    ) {
        Text(
            text = connection.name,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineLarge.fontSize
        )
        Spacer(modifier = Modifier.height(16.dp))
        if(connection.lastAlert != null){
            Text(
                text = "Last Alert: ${connection.lastAlert?.alertDate} - ${connection.lastAlert?.alertTime}",
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        }
        Row {
            Text(text = "Phone: ", fontWeight = FontWeight.Bold)
            Text(text = connection.phone, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row {
            Text(text = "Email: ", fontWeight = FontWeight.Bold)
            Text(text = connection.email, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
