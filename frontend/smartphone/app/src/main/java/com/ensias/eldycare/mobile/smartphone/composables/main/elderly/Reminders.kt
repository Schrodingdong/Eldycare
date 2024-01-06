package com.ensias.eldycare.mobile.smartphone.composables.main.elderly

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ensias.eldycare.mobile.smartphone.data.Reminder
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

@Composable
fun RemindersSectionComposable(innerPadding: PaddingValues, remindersList: List<Reminder>? = null){
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
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
    ){
        SectionTitle(text = "My\nReminders")
        RemindersList(
            if (remindersList.isNullOrEmpty()) remindersMockList else remindersList
        )
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
