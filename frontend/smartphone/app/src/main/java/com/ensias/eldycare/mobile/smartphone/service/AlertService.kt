package com.ensias.eldycare.mobile.smartphone.service

import android.util.Log
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.data.AlertType
import com.ensias.eldycare.mobile.smartphone.data.model.NotificationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Instant

class AlertService {

    /**
     * Send an alert to the server
     * @param alertType : the type of the alert
     */
    fun sendAlert(alertType: List<AlertType>) {
        GlobalScope.launch{
            // TODO : save the notification locally / cloud


            // send it
            val notif = NotificationModel(
                elderEmail= ApiClient.email,
                alertMessage=generateAlertMessage(alertType),
                alertType=alertType,
                alertTime= Instant.now().toString(),
                location="here"
            )
            Log.d("AlertService", "sendAlert: " + notif.toString())
            val res = NotificationService().sendNotification(notif)
            Log.d("AlertService", "sendAlert: " + res.toString())
        }
    }
    fun generateAlertMessage(alertType: List<AlertType>) : String {
        var alertMessage = "this person suffers from "
        for(type in alertType){
            alertMessage += type.toString() + ", "
        }
        return alertMessage
    }


    // =================================================================
    // Mocking functions
    // =================================================================
    suspend fun mockSendAlert() {
        val listOfListOfAlertTypes = listOf(
            listOf(AlertType.FALL),
            listOf(AlertType.CARDIAC),
            listOf(AlertType.FALL, AlertType.CARDIAC)
        )
        GlobalScope.launch(Dispatchers.IO){
            while(true){
                val idx = (0..2).random()
                Log.d("AlertService", "mockSendAlert: " + idx)
                sendAlert(listOfListOfAlertTypes.get(idx))
                Thread.sleep(10000)
            }
        }
    }
}