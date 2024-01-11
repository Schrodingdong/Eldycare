package com.example.android.wearable.composestarter.presentation

import android.util.Log
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType


// should save to db, to then show in the app
class PassiveDataService : PassiveListenerService(){
    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        dataPoints.getData(DataType.HEART_RATE_BPM).forEach {
            Log.d("PassiveDataService", ">> onNewDataPointsReceived: ${it.value}")

        }
    }
}
