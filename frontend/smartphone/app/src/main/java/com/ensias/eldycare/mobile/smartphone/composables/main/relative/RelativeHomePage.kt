package com.ensias.eldycare.mobile.smartphone.composables.main.relative

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDatePickerState
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
import com.ensias.eldycare.mobile.smartphone.composables.main.relative.dialogs.AddConnectionPopup
import com.ensias.eldycare.mobile.smartphone.data.Connection
import com.ensias.eldycare.mobile.smartphone.service.ConnectionService
import com.ensias.eldycare.mobile.smartphone.service.NotificationService
import com.ensias.eldycare.mobile.smartphone.service.ReminderService
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RelativeHomePage(navController: NavController, context: Context) {
    var showAddConnectionPopup by remember { mutableStateOf(false) }
    var connectionList by remember { mutableStateOf(emptyList<Connection>()) }
    var showDatePicker by remember { mutableStateOf(false) } // for date picker
    val isRefreshing by remember { mutableStateOf(false) } // Trigger the refresh by changing this state
    var hasNotificationPermission by remember { mutableStateOf(false) } // permission to send notifications
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { hasNotificationPermission = it }
    ) // request notification permission


    // Start the notification service
    LaunchedEffect(key1 = null, block = {// similar to react, execute once
        val serviceIntent = Intent(context, NotificationService::class.java)
        val connectionArrayList = ArrayList<String>()
        connectionList.forEach {
            connectionArrayList.add(it.email)
        }
        serviceIntent.putStringArrayListExtra("connection-list", connectionArrayList)
        serviceIntent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        context.startService(serviceIntent)
    })


    LaunchedEffect(Unit){
        loadConnectionList(onConnectionListChange = { connectionList = it })
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
            loadConnectionList(onConnectionListChange = { connectionList = it })
        }) {
            ConnectionsSection(
                innerPadding = innerPadding,
                connectionList = connectionList,
                showDatePickerDialog = { showDatePicker = true },
            )
        }
        if(showAddConnectionPopup){
            AddConnectionPopup(
                onDismiss = { showAddConnectionPopup = false },
                onAddConnection = { email ->
                    GlobalScope.launch {
                        ApiClient().authApi.addElderContact(email).body()?.let {
                            Log.d("RelativeHomePage", "Added connection : $it")
                            loadConnectionList(onConnectionListChange = { connectionList = it })
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
fun loadConnectionList(onConnectionListChange: (List<Connection>) -> Unit){
    GlobalScope.launch {
        // set the global connection list object
        ConnectionService.instance.loadConnectionList(onConnectionListChange)
//        // set the list to trigger composition
//        if(ConnectionService.connectionList != null)
//            onConnectionListChange(ConnectionService.connectionList!!)
//        else
//            onConnectionListChange(emptyList())
    }
}
