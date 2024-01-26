package com.ensias.eldycare.mobile.smarwatch.service

import android.util.Log
import com.google.android.gms.wearable.MessageClient

/**
 * Wrapper class for messageClient
 *
 * Used to send messages from the smartwatch to the handheld
 * @param messageClient the messageClient instance
 */
class HandheldMessageClient(
    private val messageClient: MessageClient
) {
    private val ANOMALY_DETECTION_CAPABILITY_NAME = "anomaly_detection"
    private val ANOMALY_DETECTION_CAPABILITY_PATH = "/anomaly_detection"

    fun sendMessage(message: String) {
        messageClient.sendMessage(
            ANOMALY_DETECTION_CAPABILITY_NAME,
            ANOMALY_DETECTION_CAPABILITY_PATH,
            message.toByteArray()
        ).apply {
            addOnSuccessListener {
                Log.d("HandheldMessageClient", "Message sent successfully : $message")
            }
            addOnFailureListener {
                Log.d("HandheldMessageClient", "Message failed to send")
            }
        }
    }
}
