package com.ensias.eldycare.mobile.smartphone.composables.main.elderly

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ensias.eldycare.mobile.smartphone.data.database.Alert
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun AlertSectionComposable(innerPadding: PaddingValues, alertsList: List<Alert> = emptyList()){
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
    ){
        SectionTitle(text = "My\nAlerts")
        AlertsList(alerts = alertsList)
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
        if(alerts.isEmpty()){
            // text in the middle of the screen
            Column(
                modifier = Modifier.padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                androidx.compose.material.Text(
                    text = "No alerts detected",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(16.dp))
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = "No reminders Set",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                )
            }
        } else {
            alerts.forEach { alert ->
                AlertItem(alert)
            }
        }
    }
}



@Composable
fun AlertItem(alert: Alert) {
    val dateText = alert.alertDate
    val timeText = alert.alertTime
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

