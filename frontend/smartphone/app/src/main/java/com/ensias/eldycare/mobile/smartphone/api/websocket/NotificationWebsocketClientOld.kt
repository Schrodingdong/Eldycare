package com.ensias.eldycare.mobile.smartphone.api.websocket

import android.util.Log
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import ua.naiksoftware.stomp.StompClient
import java.util.concurrent.TimeUnit

@Deprecated("use NotificationWebsocketClient instead")
class NotificationWebsocketClientOld(private val email: String) {
    companion object {
        var queueId = 0 // concurrency ?
    }
    private val client = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private var webSocket: WebSocket? = null
    private val destinationPath = "topic/alert/$email"
    private val websocketEndpoint = "notifications-ws"
    private val websocketUrl = "ws://" + ApiClient.HOSTNAME + ":" + ApiClient.PORT

    /**
     * connect to websocket && subscribe to topic && send a confirmation message
     */
    fun connect(){
        val request = Request.Builder()
            .url("$websocketUrl/$websocketEndpoint")
            .build()
        webSocket = client.newWebSocket(request, MyWebsocketListener(this))
    }

    fun disconnect() {
        webSocket?.close(1000, "Disconnecting")
    }

    /**
     * send message using STOMP protocol
     */
    fun sendMessage(message: String) {
        webSocket?.send(
            "SEND\n" +
            "destination:/$destinationPath\n" +
            "\n" +
            "$message\n" +
            "\u0000"
        )
    }

    /**
     * subscribe using STOMP protocol
     */
    fun subscribeToTopic() {
        val id = queueId++
        webSocket?.send(
            "SUBSCRIBE\n" +
                    "id:sub-"+ id +"\n" +
                    "destination:/$destinationPath\n" +
                    "\n" +
                    "\u0000"
        )
        Log.d("NotificationWebsocketClient", "id:sub-"+ id +" > Subscribed to $destinationPath")
        sendMessage("Successfully subscribed to $destinationPath")
    }


    private class MyWebsocketListener(private val client: NotificationWebsocketClientOld): WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            // Connection opened
            Log.d("NotificationWebsocketClient", "Openning Conenction to ${webSocket.request().url()}")
            val email = client.email
            // Subscribe to topic
            client.subscribeToTopic()
            // Send a message to confirm connection
            client.sendMessage("Hello $email Android!")
        }

        @OptIn(DelicateCoroutinesApi::class)
        override fun onMessage(webSocket: WebSocket, text: String) {
            GlobalScope.launch {
                delay(5000)
                Log.d("NotificationWebsocketClient", "Received text message: $text")
            }
        }

        @OptIn(DelicateCoroutinesApi::class)
        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            GlobalScope.launch {
                delay(5000)
                Log.d("NotificationWebsocketClient", "Received bytes message: ${bytes.hex()}")
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Log.d("NotificationWebsocketClient", "WebSocket closing : " + reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.d("NotificationWebsocketClient", "WebSocket closed")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            val url = webSocket.request().url().toString()
            Log.e("NotificationWebsocketClient", "WebSocket $url failure : " + t.message)
        }
    }
}