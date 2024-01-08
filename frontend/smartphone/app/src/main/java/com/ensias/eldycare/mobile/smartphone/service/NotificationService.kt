package com.ensias.eldycare.mobile.smartphone.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.api.websocket.NotificationWebsocketClient
import com.ensias.eldycare.mobile.smartphone.data.Constants
import com.ensias.eldycare.mobile.smartphone.data.api_model.NotificationResponse
import com.ensias.eldycare.mobile.smartphone.data.model.NotificationModel
import okhttp3.ResponseBody
import retrofit2.Response


class NotificationService : Service(){
    /**
     * Send notification to be pushed to the elder topic
     */
    suspend fun sendNotification(notificationModel: NotificationModel): Response<NotificationResponse>{
        return ApiClient().authApi.sendNotification(notificationModel).body()?.let {
            Response.success(it)
        } ?: Response.error(400, ResponseBody.create(null, "Error"))
    }

    lateinit var notificationManager: NotificationManager
    // For the relatives
    fun buildAlertNotification(context: Context, notificationMessage: String) {
        // Create a visually appealing and informative notification
        val customView = RemoteViews(packageName, com.ensias.eldycare.mobile.smartphone.R.layout.alert_notification_layout)
        val customViewLarge = RemoteViews(packageName, com.ensias.eldycare.mobile.smartphone.R.layout.alert_notification_layout_large)
        customView.setTextViewText(com.ensias.eldycare.mobile.smartphone.R.id.notificationText, notificationMessage)
        customViewLarge.setTextViewText(com.ensias.eldycare.mobile.smartphone.R.id.notificationText, notificationMessage)

        // Create an Intent to make a call
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:0123456789")
        val callPendingIntent = PendingIntent.getActivity(this, 0, callIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Adjust priority as needed
            .setCategory(NotificationCompat.CATEGORY_CALL) // Consider a more appropriate category
            .setCustomContentView(
                customView
            )
            .setCustomBigContentView(
                customViewLarge
            )
            .setSilent(false)
            .setContentIntent(callPendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(666, builder.build())
    }

    //====================================================================================================
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("NotificationService", "Service destroyed")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("NotificationService", "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("NotificationService", "Starting service...")
        // initialize notification channel
        initNotificationChannel()

        // get connectionList
        val connectionList = intent?.getStringArrayListExtra("connection-list")
        Log.d("NotificationService", "Connection list : $connectionList")

        // Initialize Websocket client
        initializeWebsocketClients(connectionList)

        // Show notification and run as a foreground service
        startForeground(99999999, buildServiceNotification())
        return START_STICKY
    }
    private fun buildServiceNotification(): Notification? {
        // Create a visually appealing and informative notification
        val customView = RemoteViews(packageName, com.ensias.eldycare.mobile.smartphone.R.layout.service_notification_layout)
        val builder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(com.ensias.eldycare.mobile.smartphone.R.drawable.hand_holding_hand_solid) // Use a more modern icon
            .setPriority(NotificationCompat.PRIORITY_MAX) // Adjust priority as needed
            .setCategory(NotificationCompat.CATEGORY_CALL) // Consider a more appropriate category
            .setCustomContentView(
                customView
            )
            .setSilent(true)
        return builder.build()
    }


    private fun initializeWebsocketClients(connectionList: ArrayList<String>?) {
        connectionList?.forEach {
            Log.d("NotificationService", "Connecting to : $it")
            val websocketClient = NotificationWebsocketClient(it, this, ::buildAlertNotification)
            websocketClient.connect()
        }
    }

    private fun initNotificationChannel() {
        val channel = NotificationChannel(
            Constants.CHANNEL_ID,
            "Alert Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


}