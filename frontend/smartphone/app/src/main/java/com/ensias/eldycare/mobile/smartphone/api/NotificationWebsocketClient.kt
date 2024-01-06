package com.ensias.eldycare.mobile.smartphone.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class NotificationWebsocketClient(private val email: String){
    companion object {
        var queueId = 0 // concurrency ?
    }
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val destinationPath = "topic/alert/$email"
    private val websocketEndpoint = "notifications-ws"
    private val websocketUrl = "ws://" + ApiClient.HOSTNAME + ":" + ApiClient.PORT


    suspend fun connect(){
        val request = Request.Builder()
            .url("$websocketUrl/$websocketEndpoint")
            .build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                // Connection opened
                Log.d("NotificationWebsocketClient", "WebSocket opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                // Received a text message
                Log.d("NotificationWebsocketClient", "Received text message: $text")

            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                // Received a binary message
                Log.d("NotificationWebsocketClient", "Received bytes message: ${bytes.hex()}")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                // Connection closed
                Log.d("NotificationWebsocketClient", "WebSocket closed")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                val url = webSocket.request().url().toString()
                // Connection failure
                Log.e("NotificationWebsocketClient", "WebSocket $url failure : " + t.message)
            }
        })
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
            "{\"message\":\"$message\"}\n" +
            "\u0000"
        )
    }

    /**
     * subscribe using STOMP protocol
     */
    fun subscribe() {
        val id = queueId++
        webSocket?.send(
            "SUBSCRIBE\n" +
            "id:sub-"+ id +"\n" +
            "destination:/$destinationPath\n" +
            "\n" +
            "\u0000"
        )
        Log.d("NotificationWebsocketClient", "id:sub-"+ id +" > Subscribed to $destinationPath")
        sendMessage("id:sub-${queueId-1} > Hello $email from Android!")
    }
}