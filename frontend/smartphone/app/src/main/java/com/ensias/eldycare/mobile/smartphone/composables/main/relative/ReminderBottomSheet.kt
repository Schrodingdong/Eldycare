package com.ensias.eldycare.mobile.smartphone.composables.main.relative

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ensias.eldycare.mobile.smartphone.data.Connection
import java.text.SimpleDateFormat
import java.util.Locale

// ========================================================================================
// Bottom Sheet components
// ========================================================================================
@Composable
fun BottomSheetContent(connection: Connection){
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
        BottomSheetAddReminder()
    }
}

@Composable
fun BottomSheetAddReminder(){
    var description by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 56.dp,)
    ) {
        Text(
            text = "Add Reminder",
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineLarge.fontSize
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Date", color = MaterialTheme.colorScheme.onPrimary)
            }
            Text(
                text = "no date selected",
                fontWeight = FontWeight.Light,
//                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Time", color = MaterialTheme.colorScheme.onPrimary)
            }
            Text(
                text = "no time selected",
                fontWeight = FontWeight.Light,
//                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            value = description,
            onValueChange = { description = it },
            label = { Text(text = "Description") },
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Add", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }

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
                text = "Last Alert: ${SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(connection.lastAlert?.time!!)}",
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
