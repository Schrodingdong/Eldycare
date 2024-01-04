package com.ensias.eldycare.mobile.smartphone.composables.main.relative

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.ensias.eldycare.mobile.smartphone.api.NotificationWebsocketClient

@Composable
fun RelativeHomePage(navController: NavController) {
    LaunchedEffect(Unit){
        // TODO : connect to websocket to receive info
//        NotificationWebsocketClient().connect()
    }

    Scaffold{ innerPadding ->
        ConnectionsSection(innerPadding = innerPadding)
    }
}