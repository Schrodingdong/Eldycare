package com.ensias.eldycare.mobile.smartphone.service

import android.annotation.SuppressLint
import android.util.Log
import com.ensias.eldycare.mobile.smartphone.MainActivity
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.data.AlertType
import com.ensias.eldycare.mobile.smartphone.data.database.Alert
import com.ensias.eldycare.mobile.smartphone.data.database.AlertDatabase
import com.ensias.eldycare.mobile.smartphone.data.model.NotificationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AlertService(val onAlertListChange: (List<Alert>) -> Unit) {

    /**
     * Send an alert to the server
     * @param alertTypes : the type of the alert
     */
    fun sendAlert(alertTypes: List<AlertType>) {
        GlobalScope.launch{
            // get current location
            val location = getLocation()
            // create the alert
            val alertTypeString = alertTypes.joinToString(separator = ";")
            val alert = Alert(
                elderEmail = ApiClient.email,
                alertMessage = generateAlertMessage(alertTypes),
                alertType = alertTypeString,
                alertTime = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()),
                alertDate = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now()),
                location = location // TODO : get the location
            )

            // save it Localy
            AlertDatabase.getDbInstance().alertDao.upsertAlert(alert)

            // get the new alert list
            val alertList = AlertDatabase.getDbInstance().alertDao.getAlertsByElderEmail(
                elderEmail = ApiClient.email,
                limit = 10
            ) // wont be a performance issue since the list is small && not many alerts are sent

            // refresh composition
            onAlertListChange(alertList)

            // send it
            val notif = NotificationModel(
                elderEmail= ApiClient.email,
                alertMessage=generateAlertMessage(alertTypes),
                alertType=alertTypes,
                alertTime= Instant.now().toString(),
                location= location
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

    @SuppressLint("MissingPermission")
    fun getLocation() : String {
        // TODO : POSSIBLE BUG : if the location is not yet available
        var location = ""
        MainActivity.fusedLocationClient.lastLocation.addOnSuccessListener{
            val latitude = it.latitude
            val longitude = it.longitude
            location = latitude.toString() + "," + longitude.toString()
        }
        while(location == ""){}
        return location
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