package com.ensias.eldycare.mobile.smartphone.service

import android.util.Log
import com.ensias.eldycare.mobile.smartphone.data.AlertType
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AnomalyListener (
    val alertService: AlertService
): MessageClient.OnMessageReceivedListener {
    companion object{
        private var fallCount = 0
        private var cardiacCount = 0
        private var nextStop = 0L
        private var inCoroutine = false
    }
    override fun onMessageReceived(messageEvent: MessageEvent) {
        val path = messageEvent.path
        val message = String(messageEvent.data)
        // Now you can handle the received message as needed.
        if(fallCount == 0 && nextStop == 0L){
            val waitTime = 5000
            nextStop = System.currentTimeMillis() + waitTime
        }
        if(message.contains("fall")){
            fallCount++
        }
        if (message.contains("cardiac")){
            cardiacCount++
        }
        Log.d("PhoneApp", "Received message with path: $path and content: $message. next stop : $nextStop, current time : ${System.currentTimeMillis()}")
        GlobalScope.launch {
            if(inCoroutine) return@launch
            inCoroutine = true
            while(System.currentTimeMillis() < nextStop){}
            val fallThresh = 2
            val cardiacThresh = 2
            var alertTypes = emptyList<AlertType>()
            if(fallCount >= fallThresh){
                alertTypes = alertTypes.plus(AlertType.FALL)
            }
            if(cardiacCount >= cardiacThresh){
                alertTypes = alertTypes.plus(AlertType.CARDIAC)
            }
            Log.d("PhoneApp", "Fall count: $fallCount")
            Log.d("PhoneApp", "Cardiac count: $cardiacCount")
            Log.d("PhoneApp", "Alert types: $alertTypes")
            if(alertTypes.isNotEmpty()) {
                alertService.sendAlert(alertTypes)
            }
            nextStop = 0
            fallCount = 0
            inCoroutine = false
        }
    }
}