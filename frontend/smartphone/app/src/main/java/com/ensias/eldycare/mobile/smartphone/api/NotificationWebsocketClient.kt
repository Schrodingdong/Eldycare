package com.ensias.eldycare.mobile.smartphone.api

import android.util.Log
import com.ensias.eldycare.mobile.smartphone.UserType
import com.ensias.eldycare.mobile.smartphone.service.AlertService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okio.ByteString

class NotificationWebsocketClient {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect(email: String){
        val request = Request.Builder()
            .url("ws://"+ ApiClient.BASE_URL +"/notifications-ws/topic/notifications/$email")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                // Connection opened

                println("WebSocket opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                // Received a text message
                println("Received message: $text")

            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                // Received a binary message
                println("Received binary message: $bytes")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                // Connection closed
                println("WebSocket closed: $code, $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                // Connection failure
                println("WebSocket failure: ${t.message}")
            }
        })
    }
    fun disconnect() {
        webSocket?.close(1000, "Disconnecting")
    }

    companion object {
        suspend fun init() {
            if(ApiClient.userType == UserType.ELDERLY) {
                // for mocking alerts
            } else {
                // get email list of the elders connect to you
                val emailList: List<String> = ApiClient().authApi.getElderContacts(ApiClient.email).body()!!
                Log.d("NotificationWebsocketClient", "email list : " + emailList.toString())
                // connect to each elder websocket topic
                for(email in emailList){
                    NotificationWebsocketClient().connect(email)
                }
            }
        }
    }
}