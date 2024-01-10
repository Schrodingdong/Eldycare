package com.ensias.eldycare.mobile.smartphone.composables.main.elderly

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ensias.eldycare.mobile.smartphone.MainActivity
import com.ensias.eldycare.mobile.smartphone.composables.Screen
import com.ensias.eldycare.mobile.smartphone.composables.main.TopAppBarEldycare
import com.ensias.eldycare.mobile.smartphone.data.Reminder
import com.ensias.eldycare.mobile.smartphone.data.model.ReminderCalendarEventModel
import com.ensias.eldycare.mobile.smartphone.service.AlertService
import com.ensias.eldycare.mobile.smartphone.service.NotificationService
import com.ensias.eldycare.mobile.smartphone.service.ReminderService
import com.ensias.eldycare.mobile.smartphone.service.content_provider.CalendarProvider
import java.time.Instant
import java.util.Date


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

    // Start the reminders service
    LaunchedEffect(key1 = null, block = {
        val serviceIntent = Intent(context, ReminderService::class.java)
        serviceIntent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        context.startService(serviceIntent)
    })

    LaunchedEffect(Unit){
        // TODO ACTIVATE THE MOCKS
//        AlertService().mockSendAlert()
        // Read from calendar
        ActivityCompat.requestPermissions(
            context as MainActivity,
            arrayOf(
                android.Manifest.permission.READ_CALENDAR,
                android.Manifest.permission.WRITE_CALENDAR,
            ),
            101
        )
        CalendarProvider(context).readFromCalendar().forEach{
            val reminderEl = Reminder(
                time = Date.from(Instant.ofEpochMilli(it.dtstart)),
                description = it.title.subSequence(
                    startIndex = ReminderCalendarEventModel.TITLE_PREFIX.length,
                    endIndex = it.title.length
                ).toString()
            )
            reminderList = reminderList + reminderEl
        }
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
        }
    ){ innerPadding ->
        if(section == Section.REMINDERS){
            RemindersSectionComposable(innerPadding = innerPadding, remindersList = reminderList)
        } else if(section == Section.ALERTS){
            AlertSectionComposable(innerPadding = innerPadding)
        }
    }
}

@Preview
@Composable
fun ElderHomePagePreview(){
    Scaffold(
        topBar = {
            TopAppBarEldycare()
        },
    ){ innerPadding ->
        RemindersSectionComposable(innerPadding = innerPadding)
    }
}