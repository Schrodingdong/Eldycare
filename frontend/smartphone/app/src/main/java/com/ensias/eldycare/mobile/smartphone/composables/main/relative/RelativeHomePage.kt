package com.ensias.eldycare.mobile.smartphone.composables.main.relative

import android.Manifest
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.api.websocket.NotificationWebsocketClient
import com.ensias.eldycare.mobile.smartphone.composables.main.TopAppBarEldycare
import com.ensias.eldycare.mobile.smartphone.data.Connection
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun RelativeHomePage(navController: NavController, context: Context) {
    var showAddConnectionPopup by remember { mutableStateOf(false) }
    var connectionList by remember { mutableStateOf(emptyList<Connection>()) }
    // Trigger the refresh by changing this state
    val isRefreshing by remember { mutableStateOf(false) }
    // permission to send notifications
    var hasNotificationPermission by remember { mutableStateOf(false) }
    // request notification permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { hasNotificationPermission = it }
    )

    LaunchedEffect(Unit){
        loadConnectionList(onConnectionListChange = { connectionList = it }, context = context)
        Log.d("RelativeHomePage", "Requesting notification permission")
        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    Scaffold(
        topBar = { TopAppBarEldycare() },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showAddConnectionPopup = true
            }) {
                Icon(Icons.Outlined.Add, contentDescription = null)
            }
        }
    ){ innerPadding ->
        SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = isRefreshing), onRefresh = {
            loadConnectionList(onConnectionListChange = { connectionList = it }, context = context)
        }) {
            ConnectionsSection(
                innerPadding = innerPadding,
                connectionList = connectionList
            )
        }
        if(showAddConnectionPopup){
            AddConnectionPopup(
                onDismiss = { showAddConnectionPopup = false },
                onAddConnection = { email ->
                    GlobalScope.launch {
                        ApiClient().authApi.addElderContact(email).body()?.let {
                            Log.d("RelativeHomePage", "Added connection : $it")
                            loadConnectionList(onConnectionListChange = { connectionList = it }, context = context)
                        }
                    }.let {
                        showAddConnectionPopup = false
                    }
                }
            )
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun loadConnectionList(onConnectionListChange: (List<Connection>) -> Unit, context: Context) {
    GlobalScope.launch {
        ApiClient().authApi.getElderContacts().body()?.let {
            Log.d("RelativeHomePage", "Got connection list : $it")
            val newConnections = it.map {
                Connection(
                    email = it.email,
                    name = it.username,
                    phone = if(it.phone == null) "" else it.phone, // TODO phone is never nullable
                    lastAlert = null // TODO
                )
            }
            // set the list to trigger composition
            onConnectionListChange(newConnections)

            // TODO : subscribe to the new connections with websockets
            newConnections.forEach {
                Log.d("RelativeHomePage", "Subscribing to ${it.email}")
                val websocketClient = NotificationWebsocketClient(it.email, context = context)
                websocketClient.connect()
            }

        }

    }
}

@Composable
fun AddConnectionPopup(onDismiss: () -> Unit, onAddConnection: (String) -> Unit) {
    var elderEmail by remember { mutableStateOf("") }
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.5f)
    ){
        Dialog(
            onDismissRequest = onDismiss,
        ){
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                Column(
                    modifier = Modifier.padding(16.dp)
                ){
                    Text(text = "Add a connection", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = "Who do you want to add ?")
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = elderEmail,
                        onValueChange = { elderEmail = it },
                        label = { Text("Elder Email") }
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        Button(onClick = {
                            onAddConnection(elderEmail)
                            onDismiss()
                            // TODO : should refresh the list
                        }) {
                            Text(text = "Add")
                        }
                    }

                }

            }
        }
    }
}

