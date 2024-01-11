package com.ensias.eldycare.mobile.smartphone.composables.main.relative

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.composables.main.TopAppBarEldycare
import com.ensias.eldycare.mobile.smartphone.composables.main.relative.dialogs.AddConnectionPopup
import com.ensias.eldycare.mobile.smartphone.data.Connection
import com.ensias.eldycare.mobile.smartphone.service.ConnectionService
import com.ensias.eldycare.mobile.smartphone.service.NotificationService
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RelativeHomePage(navController: NavController, context: Context) {
    val mContext = LocalContext.current
    var showAddConnectionPopup by remember { mutableStateOf(false) }
    var connectionList by remember { mutableStateOf(listOf<Connection>()) }
    var showDatePicker by remember { mutableStateOf(false) } // for date picker
    val isRefreshing by remember { mutableStateOf(false) } // Trigger the refresh by changing this state
    var hasNotificationPermission by remember { mutableStateOf(false) } // permission to send notifications
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { hasNotificationPermission = it }
    ) // request notification permission


    var firstLaunch = true
    LaunchedEffect(Unit){
        loadConnectionList(onConnectionListChange = { connectionList = it })
        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        if (firstLaunch){
            firstLaunch = false
            startNotificationService(context, connectionList)
        }
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
            GlobalScope.launch {
                Log.d("RelativeHomePage", "Refreshing connection list")
                loadConnectionList(onConnectionListChange = { connectionList = it })
            }
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
                onAddConnection = { elderEmail ->
                    GlobalScope.launch {
                        ApiClient().authApi.addElderContact(elderEmail).let {
                            if (!it.isSuccessful) {
                                Log.d("RelativeHomePage", "Problem adding connection : ${it.body()}")
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        mContext,
                                        "Problem adding connection",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                return@let
                            } else {
                                Log.d("RelativeHomePage", "Added connection : ${it.body()}")
                                loadConnectionList(onConnectionListChange = { connectionList = it })

                                // to ArrayList<String>
                                val connectionArrayList = ArrayList<String>()
                                connectionList.forEach {
                                    connectionArrayList.add(it.email)
                                }
                                // initialise the notification websocket
                                NotificationService.instance!!.initializeWebsocketClients(connectionArrayList)

                                // make it run on  UI thread
                                withContext(Dispatchers.Main){
                                    Toast.makeText(mContext, "Connection added", Toast.LENGTH_SHORT).show()
                                }

                            }
                        }

                    }.let {
                        showAddConnectionPopup = false
                    }
                }
            )
        }
    }
}


suspend fun loadConnectionList(onConnectionListChange: (List<Connection>) -> Unit){
    val retrievedConnectionList: List<Connection>
    runBlocking {
        retrievedConnectionList = ConnectionService.loadConnectionList()
    }
    onConnectionListChange(retrievedConnectionList)             // change for composition
    ConnectionService.connectionList = retrievedConnectionList  // change for service
}

private fun startNotificationService(
    context: Context,
    connectionList: List<Connection>
) {
    val serviceIntent = Intent(context, NotificationService::class.java)
    val connectionArrayList = ArrayList<String>()
    connectionList.forEach {
        connectionArrayList.add(it.email)
    }
    Log.d(
        "RelativeHomePage",
        "Starting notification service with connection list : $connectionArrayList"
    )
    serviceIntent.putStringArrayListExtra("connection-list", connectionArrayList)
    serviceIntent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
    context.startService(serviceIntent)
}
