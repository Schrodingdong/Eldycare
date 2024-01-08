package com.ensias.eldycare.mobile.smartphone.api.websocket

import android.content.Context
import android.util.Log
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.api.AppOkHttpClient
import com.ensias.eldycare.mobile.smartphone.data.model.NotificationModel
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient


/**
 * Uses Stomp protocol to connect to websocket
 * @param email : elder email to subscribe to
 */
class NotificationWebsocketClient(val email: String, val context: Context, val messageCallback: (Context, NotificationModel) -> Unit){
//    private val websocketUrl = "ws://" + ApiClient.HOSTNAME + ":" + ApiClient.PORT + "/notification"
    private val websocketUrl = "ws://" + ApiClient.HOSTNAME + ":8082" // TODO : change with 8888 above
    private val websocketEndpoint = "notifications-ws"
    private val alertTopic = "/topic/alert/$email"

    private val compositeDisposable = CompositeDisposable()
    private lateinit var stompClient: StompClient

    @OptIn(DelicateCoroutinesApi::class)
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
                            // parse the message into a notification
                            val notificationReceived: NotificationModel = ApiClient.gson.fromJson(topicMessage.payload, NotificationModel::class.java)
                            // show notification
                            messageCallback(context, notificationReceived)
                        },
                        { throwable ->
                            Log.e("NotificationWebsocketClient", "Error on subscribe topic", throwable)
                        }
                    )
                compositeDisposable.add(subscribe)
            } else {
                Log.e("NotificationWebsocketClient", "Failed to connect to websocket")
            }
        }
    }

    fun disconnect(){
        stompClient.disconnect()
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }
}

