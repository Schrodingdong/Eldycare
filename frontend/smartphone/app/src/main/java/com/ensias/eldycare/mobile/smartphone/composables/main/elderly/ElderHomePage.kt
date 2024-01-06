package com.ensias.eldycare.mobile.smartphone.composables.main.elderly

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ensias.eldycare.mobile.smartphone.composables.Screen
import com.ensias.eldycare.mobile.smartphone.composables.main.TopAppBarEldycare
import com.ensias.eldycare.mobile.smartphone.service.AlertService


enum class Section {
    REMINDERS,
    ALERTS
}

@Composable
fun ElderHomePage(navController: NavController){
    val items = listOf(
        Screen.RemindersPage,
        Screen.AlertsPage
    )
    var section by remember { mutableStateOf(Section.REMINDERS) }

    LaunchedEffect(Unit){
        AlertService().mockSendAlert()
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
            RemindersSectionComposable(innerPadding = innerPadding)
        } else if(section == Section.ALERTS){
            AlertSectionComposable(innerPadding = innerPadding)
        }
    }
}