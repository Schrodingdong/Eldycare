package com.ensias.eldycare.mobile.smartphone.composables.main.elderly

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ensias.eldycare.mobile.smartphone.composables.Screen
import com.ensias.eldycare.mobile.smartphone.data.Alert
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date


@Composable
fun AlertSectionComposable(innerPadding: PaddingValues, alertsList: List<Alert>? = null){
    val alertsMockList= listOf(
        Alert(Date.from(Instant.now()), "Alert 1"),
        Alert(Date.from(Instant.now()), "Alert 2"),
        Alert(Date.from(Instant.now()), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquam nisl nunc eu nisl. Donec euismod, nisl eget "),
        Alert(Date.from(Instant.now()), "Alert 4"),
        Alert(Date.from(Instant.now()), "Alert 5"),
        Alert(Date.from(Instant.now()), "Alert 6"),
        Alert(Date.from(Instant.now()), "Alert 7"),
        Alert(Date.from(Instant.now()), "Alert 8"),
        Alert(Date.from(Instant.now()), "Alert 9"),
        Alert(Date.from(Instant.now()), "Alert 10"),
    )
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
    ){
        TopDecorationSimple("My\nReminders")
        SectionTitle(text = "My\nAlerts")
        AlertsList(
            if(alertsList.isNullOrEmpty()) alertsMockList else alertsList
        )
    }

}

@Composable
fun AlertsList(alerts: List<Alert> = emptyList()) {
    Column (
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 8.dp)
    ){
        alerts.forEach { alert ->
            AlertItem(alert)
        }
    }
}



@Composable
fun AlertItem(alert: Alert) {
    val dateText = SimpleDateFormat("dd/MM/yyyy").format(alert.time)
    val timeText = "At " + SimpleDateFormat("HH:mm").format(alert.time)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.error,
        )
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ){
                Icon(Icons.Filled.Warning, contentDescription = null, modifier = Modifier.padding(end = 8.dp), tint = MaterialTheme.colorScheme.onError)
                Column {
                    Text("Alert detected at", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onError)
                    Text(text = dateText + " - " + timeText, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onError)
                }

            }
        }
    }
}

