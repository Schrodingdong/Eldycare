package com.ensias.eldycare.mobile.smartphone.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.api.websocket.ReminderWebsocketClient
import com.ensias.eldycare.mobile.smartphone.data.model.ReminderModel

class ReminderService : Service(){
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("ReminderService", "ReminderService created")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ReminderService", "ReminderService destroyed")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ReminderService", "Starting ReminderService...")
        // initialize websocket client
        initializeWebsocketClient()
        return START_STICKY
    }

    private fun initializeWebsocketClient() {
        // get the user email
        val email = ApiClient.email
        val websocketClient = ReminderWebsocketClient(email, this, ::reminderHandler)
        websocketClient.connect()
    }

    private fun reminderHandler(context : Context, reminderModel : ReminderModel) {
        Log.d("ReminderService", "Reminder received : $reminderModel")
    }
}