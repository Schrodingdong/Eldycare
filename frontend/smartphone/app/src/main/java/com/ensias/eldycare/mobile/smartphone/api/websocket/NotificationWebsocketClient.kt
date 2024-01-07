package com.ensias.eldycare.mobile.smartphone.api.websocket

import android.util.Log
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient


/**
 * Uses Stomp protocol to connect to websocket
 * @param email : elder email to subscribe to
 */
class NotificationWebsocketClient(val email: String){
    private val websocketUrl = "ws://" + ApiClient.HOSTNAME + ":" + ApiClient.PORT
    private val websocketEndpoint = "notifications-ws"
    private val alertTopic = "/topic/alert/$email"
    private var stompClient: StompClient? = null

    fun connect(){
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "$websocketUrl/$websocketEndpoint")
        stompClient?.connect()

        val subscribe = stompClient?.topic(alertTopic)?.subscribe { message ->
            Log.d("NotificationWebsocketClient", "Received message: ${message.payload}")
            // TODO : handle message
        }
    }
}

