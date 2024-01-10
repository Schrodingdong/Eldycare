package com.ensias.eldycare.mobile.smartphone.api.websocket

import android.content.Context
import android.util.Log
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.api.AppOkHttpClient
import com.ensias.eldycare.mobile.smartphone.data.api_model.ReminderRequest
import com.ensias.eldycare.mobile.smartphone.data.model.ReminderModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ReminderWebsocketClient(val email: String, val context: Context, val messageCallback: (Context, ReminderModel) -> Unit){
//    private val websocketUrl = "ws://" + ApiClient.HOSTNAME + ":" + ApiClient.PORT + "/notification"
    private val websocketUrl = "ws://" + ApiClient.HOSTNAME + ":8083" // TODO : change with 8888 above
    private val websocketEndpoint = "reminders-ws"
    private val alertTopic = "/topic/reminder/$email"


    private val compositeDisposable = CompositeDisposable()
    private lateinit var stompClient: StompClient


    fun connect(){
        // initialize stomp client
        stompClient = Stomp.over(
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
                val subscribe = stompClient.topic(alertTopic)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { topicMessage ->
                            // parse the message into a reminder
                            Log.d("ReminderWebsocketClient", "AA${topicMessage.payload}")
                            val reminderReceived: ReminderRequest = ApiClient.gson.fromJson(topicMessage.payload, ReminderRequest::class.java)
                            val reminderModel = ReminderModel(
                                reminderReceived.elderEmail,
                                reminderReceived.relativeEmail,
                                LocalDate.parse(reminderReceived.reminderDate, DateTimeFormatter.ISO_LOCAL_DATE),
                                LocalTime.parse(reminderReceived.reminderTime, DateTimeFormatter.ofPattern("HH:mm:ss")),
                                reminderReceived.description
                            )
                            messageCallback(context, reminderModel)
                        },
                        { throwable ->
                            Log.e("ReminderWebsocketClient", "Error on subscribe topic", throwable)
                        }
                    )
                compositeDisposable.add(subscribe)
            } else {
                Log.e("ReminderWebsocketClient", "Failed to connect to websocket")
            }
        }
    }
}