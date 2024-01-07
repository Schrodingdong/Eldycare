package com.ensias.eldycare.mobile.smartphone.api.websocket

import android.util.Log
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.api.AppOkHttpClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent


/**
 * Uses Stomp protocol to connect to websocket
 * @param email : elder email to subscribe to
 */
class NotificationWebsocketClient(val email: String){
//    private val websocketUrl = "ws://" + ApiClient.HOSTNAME + ":" + ApiClient.PORT + "/notification"
    private val websocketUrl = "ws://" + ApiClient.HOSTNAME + ":8082" // TODO : change with 8888 above
    private val websocketEndpoint = "notifications-ws"
    private val alertTopic = "/topic/alert/$email"

    @OptIn(DelicateCoroutinesApi::class)
    fun connect(){
        // initialize stomp client
        val stompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "$websocketUrl/$websocketEndpoint",
            null,
            AppOkHttpClient.client
        )

        // connect to websocket
        stompClient.connect()

        GlobalScope.launch {
            // wait for stomp client to connect
            while(!stompClient.isConnected){
                delay(500)
            }

            // subscribe to alert topic
            if(stompClient.isConnected){
                val subscribe = stompClient?.topic(alertTopic)?.subscribe { message ->
                    Log.d("NotificationWebsocketClient", "Received message: ${message.payload}")
                    // TODO : handle message
                }
            } else {
                Log.e("NotificationWebsocketClient", "Failed to connect to websocket")
            }
        }
    }
}

