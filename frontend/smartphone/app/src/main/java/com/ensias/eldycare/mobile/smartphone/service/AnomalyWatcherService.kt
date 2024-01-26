package com.ensias.eldycare.mobile.smartphone.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.ensias.eldycare.mobile.R
import com.ensias.eldycare.mobile.smartphone.data.Constants
import com.google.android.gms.wearable.Wearable

class AnomalyWatcherService : Service(){
    companion object{
        var context: Context? = null
        var alertService: AlertService? = null
        private lateinit var notificationManager: NotificationManager
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (context == null || alertService == null) return START_STICKY
        // init notification channel
        initNotificationChannel()

        // init anomaly listener
        val anomalyListener = AnomalyListener(alertService!!)
        Wearable.getMessageClient(context!!).addListener(anomalyListener)

        // start foreground service
        startForeground(99999998, buildServiceNotification())
        return START_STICKY
    }

    private fun buildServiceNotification(): Notification? {
        // Create a visually appealing and informative notification
        val customView = RemoteViews(packageName, R.layout.service_notification_layout)
        val builder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(com.ensias.eldycare.mobile.R.drawable.hand_holding_hand_solid) // Use a more modern icon
            .setPriority(NotificationCompat.PRIORITY_MAX) // Adjust priority as needed
            .setCategory(NotificationCompat.CATEGORY_CALL) // Consider a more appropriate category
            .setCustomContentView(
                customView
            )
            .setSilent(true)
        return builder.build()
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