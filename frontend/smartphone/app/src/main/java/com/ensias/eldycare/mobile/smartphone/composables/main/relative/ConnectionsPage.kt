package com.ensias.eldycare.mobile.smartphone.composables.main.relative

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
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
import com.ensias.eldycare.mobile.smartphone.data.Connection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionsSection(innerPadding: PaddingValues, connectionList: List<Connection>? = null){
    var showBottomSheet by remember { mutableStateOf(false) }
    val clickedConnection by remember { mutableStateOf(Connection("","", "")) }
    val sheetState = rememberModalBottomSheetState()

    Surface {
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ){
            item {
                SectionTitle(text = "My\nConnections")
            }
            if (connectionList != null) {
                items(connectionList.size){
                    ConnectionItem(
                        connection = connectionList[it],
                        onConnectionClick = {showBottomSheet = true},
                        clickedConnection = clickedConnection
                    )
                }
            }
            else {
                item{
                    Icon(Icons.Filled.Person, contentDescription = null)
                    Text(text = "No connections yet")
                }
            }
//        ConnectionsList(
//            connectionList = if(connectionList.isNullOrEmpty()) connectionMockList else connectionList,
//            onConnectionClick = { showBottomSheet = true },
//            clickedConnection = clickedConnection
//        )

        }
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

//@Composable
//fun ConnectionsList(connectionList: List<Connection> = emptyList(), onConnectionClick: () -> Unit, clickedConnection: Connection) {
//    LazyColumn(
//        modifier = Modifier
//            .verticalScroll(rememberScrollState())
//            .fillMaxHeight()
//            .fillMaxWidth()
//            .padding(start = 32.dp, end = 32.dp, bottom = 8.dp)
//    ){
//
//        items(connectionList.size){
//            ConnectionItem(connectionList[it], onConnectionClick, clickedConnection)
//            Divider()
//        }
////        connectionList.forEach { connection ->
////            ConnectionItem(connection, onConnectionClick, clickedConnection)
////        }
//    }
//}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionItem(connection: Connection, onConnectionClick: () -> Unit, clickedConnection: Connection) {
    Log.d("ConnectionItem", "ConnectionItem: ${connection.toString()}")
    OutlinedCard (
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 32.dp, vertical = 8.dp)),
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


