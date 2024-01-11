package com.ensias.eldycare.mobile.smartphone.composables.main.elderly

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ensias.eldycare.mobile.smartphone.MainActivity
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.composables.Screen
import com.ensias.eldycare.mobile.smartphone.composables.main.TopAppBarEldycare
import com.ensias.eldycare.mobile.smartphone.composables.main.relative.dialogs.MyDatePickerDialog
import com.ensias.eldycare.mobile.smartphone.composables.main.relative.dialogs.MyTimePickerDialog
import com.ensias.eldycare.mobile.smartphone.data.Reminder
import com.ensias.eldycare.mobile.smartphone.data.database.Alert
import com.ensias.eldycare.mobile.smartphone.data.database.AlertDatabase
import com.ensias.eldycare.mobile.smartphone.data.model.ReminderCalendarEventModel
import com.ensias.eldycare.mobile.smartphone.data.model.ReminderModel
import com.ensias.eldycare.mobile.smartphone.service.AlertService
import com.ensias.eldycare.mobile.smartphone.service.ReminderService
import com.ensias.eldycare.mobile.smartphone.service.content_provider.CalendarProvider
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


enum class Section {
    REMINDERS,
    ALERTS
}


@Composable
fun ElderHomePage(navController: NavController, context: Context){
    val items = listOf(
        Screen.RemindersPage,
        Screen.AlertsPage
    )
    var section by remember { mutableStateOf(Section.REMINDERS) }
    var reminderList by remember { mutableStateOf(listOf<Reminder>()) }
    var alertList by remember { mutableStateOf(listOf<Alert>()) }
    val alertService = AlertService(onAlertListChange = {alertList = it})

    // reminder variables
    var showAddReminderPopup by remember { mutableStateOf(false) }
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()
    var reminder by remember {
        mutableStateOf(ReminderModel(
            reminderDate = LocalDate.now(),
            reminderTime = LocalTime.now(),
            description = "",
            elderEmail = ApiClient.email,
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


    // Start the reminders service
    LaunchedEffect(key1 = null, block = {
        ReminderService.onReminderListChange = {reminderList = it}
        val serviceIntent = Intent(context, ReminderService::class.java)
        // put variable for the service
        serviceIntent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        context.startService(serviceIntent)
    })

    LaunchedEffect(Unit){
        // TODO ACTIVATE THE MOCKS
        alertService.mockSendAlert()

        // request permissions
        ActivityCompat.requestPermissions(
            context as MainActivity,
            arrayOf(
                android.Manifest.permission.READ_CALENDAR,
                android.Manifest.permission.WRITE_CALENDAR,
            ),
            101
        )

        // Read reminders from calendar
        ReminderService.recomposeReminderList(context)
    }

    Scaffold(
        topBar = {
            TopAppBarEldycare()
        },
        bottomBar = {
            BottomNavigation(
                elevation = 16.dp,
                backgroundColor = Color.White
            ){
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            when(screen){
                                Screen.RemindersPage -> Icon(Icons.Filled.Home, contentDescription = null)
                                Screen.AlertsPage -> Icon(Icons.Filled.Warning, contentDescription = null)
                                else -> Icon(Icons.Filled.Info, contentDescription = null)
                            }
                        },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        unselectedContentColor = Color.Gray,
                        selectedContentColor = Color.Black,
                        onClick = {
                            section = when(screen){
                                Screen.RemindersPage -> Section.REMINDERS
                                Screen.AlertsPage -> Section.ALERTS
                                else -> Section.REMINDERS
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            if(section == Section.REMINDERS)
                FloatingActionButton(onClick = {
                    showAddReminderPopup = true
                }) {
                    Icon(Icons.Outlined.Add, contentDescription = null, tint = Color.White)
                }
        }
    ){ innerPadding ->
        if(section == Section.REMINDERS){
            RemindersSectionComposable(innerPadding = innerPadding, remindersList = reminderList)
        } else if(section == Section.ALERTS){
            AlertSectionComposable(innerPadding = innerPadding, alertsList = alertList)
        }
    }
    if (showAddReminderPopup) {
        AddReminderPopup(
            onDismiss = { showAddReminderPopup = false },

            reminderModel = reminder,
            onReminderChange = { reminder = it },
            formattedDate = formattedDate,
            formattedTime = formattedTime,
            dateDialogState = dateDialogState,
            showDateDialog = { showDateDialog ->
                if (showDateDialog) {
                    dateDialogState.show()
                } else {
                    dateDialogState.hide()
                }
            },
            timeDialogState = timeDialogState,
            showTimeDialog = { showTimeDialog ->
                if (showTimeDialog) {
                    timeDialogState.show()
                } else {
                    timeDialogState.hide()
                }
            },
            onAddReminder = { reminder ->
                // save locally to calendar
                val reminderCalendarEvent = ReminderCalendarEventModel(
                    title = "${ReminderCalendarEventModel.TITLE_PREFIX}${reminder.description}",
                    dtstart = reminder.reminderDate.atTime(reminder.reminderTime).toInstant(ZoneOffset.UTC).toEpochMilli(),
                    dtend = reminder.reminderDate.atTime(reminder.reminderTime).toInstant(ZoneOffset.UTC).toEpochMilli(),
                    description = reminder.description,
                    calendarId = 1,
                    eventTimezone = "UTC",
                    eventLocation = "ELDYCARE"
                )
                CalendarProvider(context).writeToCalendar(reminderCalendarEvent)

                // recomposition
                ReminderService.recomposeReminderList(context)
            }
        )
    }
}

