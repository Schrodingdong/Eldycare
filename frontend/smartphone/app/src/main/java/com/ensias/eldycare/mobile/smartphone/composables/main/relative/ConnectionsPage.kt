package com.ensias.eldycare.mobile.smartphone.composables.main.relative

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ensias.eldycare.mobile.smartphone.composables.main.elderly.TopDecorationSimple
import com.ensias.eldycare.mobile.smartphone.data.Alert
import com.ensias.eldycare.mobile.smartphone.data.Connection
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ConnectionsPage(navController: NavController) {
//    val connectionMockList = listOf(
//        Connection("John Doe", "john@gmail.com", "1234567890", Alert(Date.from(Instant.now()), "Alert 1")),
//        Connection("Jane Doe", "jane@gmail.com", "1234567890"),
//        Connection("John Doe", "john@gmail.com", "1234567890"),
//        Connection("Jane Doe", "jane@gmail.com", "1234567890"),
//    )
//    var showBottomSheet by remember { mutableStateOf(false) }
//    var clickedConnection by remember { mutableStateOf(Connection("","", "")) }
//    var sheetState = rememberModalBottomSheetState()
//
//    Scaffold{ innerPadding ->
//        Column(
//            modifier = Modifier
//                .padding(innerPadding)
//                .fillMaxWidth()
//        ){
//            TopDecorationSimple("My\nReminders")
//            SectionTitle(text = "My\nConnections")
//            ConnectionsList(connectionMockList, { showBottomSheet = true }, clickedConnection)
//
//            // bottom sheet logic
//            if(showBottomSheet){
//                ModalBottomSheet(
//                    containerColor = MaterialTheme.colorScheme.surface,
//                    onDismissRequest = { showBottomSheet = false },
//                    sheetState = sheetState,
//                ){
//                    BottomSheetContent(clickedConnection)
//                }
//            }
//        }
//    }
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionsSection(innerPadding: PaddingValues, connectionList: List<Connection>? = null){
    var showBottomSheet by remember { mutableStateOf(false) }
    var clickedConnection by remember { mutableStateOf(Connection("","", "")) }
    var sheetState = rememberModalBottomSheetState()
    val connectionMockList = listOf(
        Connection("John Doe", "john@gmail.com", "1234567890", Alert(Date.from(Instant.now()), "Alert 1")),
        Connection("Jane Doe", "jane@gmail.com", "1234567890"),
        Connection("John Doe", "john@gmail.com", "1234567890"),
        Connection("Jane Doe", "jane@gmail.com", "1234567890"),
    )
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
    ){
        TopDecorationSimple("My\nReminders")
        SectionTitle(text = "My\nConnections")
        ConnectionsList(
            connectionList = if(connectionList.isNullOrEmpty()) connectionMockList else connectionList,
            onConnectionClick = { showBottomSheet = true },
            clickedConnection = clickedConnection
        )

        // bottom sheet logic
        if(showBottomSheet){
            ModalBottomSheet(
                containerColor = MaterialTheme.colorScheme.surface,
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
            ){
                BottomSheetContent(clickedConnection)
            }
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
fun ConnectionsList(connectionList: List<Connection> = emptyList(), onConnectionClick: () -> Unit, clickedConnection: Connection) {
    Column (
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 8.dp)
    ){
        connectionList.forEach { connection ->
            ConnectionItem(connection, onConnectionClick, clickedConnection)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionItem(connection: Connection, onConnectionClick: () -> Unit, clickedConnection: Connection) {
    OutlinedCard (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            /*TODO : make a bottom sheet appear with the needed info on screen */
            clickedConnection.name = connection.name
            clickedConnection.phone = connection.phone
            clickedConnection.email = connection.email
            clickedConnection.lastAlert = connection.lastAlert
            onConnectionClick()
        }
    ){
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){
                Icon(Icons.Filled.Person, contentDescription = "Connection Icon", modifier = Modifier.padding(end = 8.dp) )
                Column (
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(text = connection.name, fontWeight = FontWeight.Bold)
                    Text(text = connection.phone, fontWeight = FontWeight.Light, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

            }
//            Button(
////                modifier = Modifier.width(25.dp),
//                onClick = { /*TODO*/ }
//            ) {
//                Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Connection Icon", tint = MaterialTheme.colorScheme.onPrimary)
//            }
        }
    }
}


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
            .padding(start = 32.dp, end = 32.dp, bottom = 56.dp, )
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
                text = "Last Alert: ${SimpleDateFormat("dd/MM/yyyy").format(connection.lastAlert?.time!!)}",
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
