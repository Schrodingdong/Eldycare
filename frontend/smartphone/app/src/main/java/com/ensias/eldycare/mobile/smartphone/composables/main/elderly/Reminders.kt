package com.ensias.eldycare.mobile.smartphone.composables.main.elderly

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ensias.eldycare.mobile.smartphone.R
import com.ensias.eldycare.mobile.smartphone.composables.Screen
import com.ensias.eldycare.mobile.smartphone.data.Reminder
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

@Composable
fun RemindersPage(navController: NavController) {
    val items = listOf(
        Screen.RemindersPage,
        Screen.AlertsPage
    )

    val remindersMockList= listOf(
        Reminder(Date.from(Instant.now()), "Reminder 1"),
        Reminder(Date.from(Instant.now()), "Reminder 2"),
        Reminder(Date.from(Instant.now()), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquam nisl nunc eu nisl. Donec euismod, nisl eget "),
        Reminder(Date.from(Instant.now()), "Reminder 4"),
        Reminder(Date.from(Instant.now()), "Reminder 5"),
        Reminder(Date.from(Instant.now()), "Reminder 6"),
        Reminder(Date.from(Instant.now()), "Reminder 7"),
        Reminder(Date.from(Instant.now()), "Reminder 8"),
        Reminder(Date.from(Instant.now()), "Reminder 9"),
        Reminder(Date.from(Instant.now()), "Reminder 10"),
        Reminder(Date.from(Instant.now()), "Reminder 11"),
    )
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                /*TODO*/
                // Add the possibility to add a reminder using the Calendar API
            }) {
                Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White)
            }
        },
        bottomBar = {
            BottomNavigation(
                elevation = 8.dp,
                backgroundColor = MaterialTheme.colorScheme.surface
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                        label = { androidx.compose.material3.Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedContentColor = MaterialTheme.colorScheme.onSurface,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ){
            TopDecorationSimple("My\nReminders")
            SectionTitle(text = "My\nReminders")
            RemindersList(remindersMockList)
        }
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
        reminders.forEach { reminder ->
            ReminderItem(reminder)
        }
    }
}



@Composable
fun ReminderItem(reminder: Reminder) {
    val dateText = SimpleDateFormat("dd/MM/yyyy").format(reminder.time)
    val timeText = "At " + SimpleDateFormat("HH:mm").format(reminder.time)
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
        }
    }
}

@Composable
fun TopDecorationSimple(text: String){
    val s = text.split('\n')
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            Box(
                contentAlignment = Alignment.Center,
            ){
                Box {
                    Image(painter = painterResource(id = R.drawable.top_decor_rectangle), contentDescription = "top_decor_rectangle")
                }
                Text(
                    text = "ELDYCARE",
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    color = Color.White,
                )
            }
        }
    }
}
