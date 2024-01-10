package com.ensias.eldycare.mobile.smartphone.composables.main.elderly

import android.content.res.Resources.Theme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Colors
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ensias.eldycare.mobile.smartphone.R
import com.ensias.eldycare.mobile.smartphone.data.Reminder
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
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
    val dateText = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(reminder.time)
    val timeText = "At " + SimpleDateFormat("HH:mm", Locale.FRENCH).format(reminder.time)
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
