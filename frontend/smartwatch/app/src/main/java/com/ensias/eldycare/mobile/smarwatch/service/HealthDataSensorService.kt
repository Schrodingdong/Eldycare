package com.ensias.eldycare.mobile.smarwatch.service

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerCallback
import androidx.health.services.client.PassiveMonitoringClient
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.getCapabilities
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HealthDataSensorService(
    private val context : Activity,
){
    companion object {
        val heartRateFlow = MutableStateFlow(0)
        var passiveMonitoringClient : PassiveMonitoringClient? = null
        var requiredPermission : String? = null
    }
    init {
        getPassiveMonitoringClient()
        requestHealthSensorPermissions()

        // Proceed with accessing health data
        if (ContextCompat.checkSelfPermission(context, requiredPermission!!) == PackageManager.PERMISSION_GRANTED) {
            accessHealthData()
        }
    }
    fun requestHealthSensorPermissions(){
        requiredPermission = android.Manifest.permission.BODY_SENSORS
        ActivityCompat.requestPermissions(context, arrayOf(requiredPermission), 0)
    }
    fun getPassiveMonitoringClient(){
        val healthClient = HealthServices.getClient(context)
        passiveMonitoringClient = healthClient.passiveMonitoringClient
    }
    fun accessHealthData(){
        GlobalScope.launch {
            val capabilities = passiveMonitoringClient!!.getCapabilities()
            val supportsHeartRate = DataType.HEART_RATE_BPM in capabilities.supportedDataTypesPassiveMonitoring
            if(supportsHeartRate){
                setPassiveHeartRateListener()
            }
        }

    }
    fun setPassiveHeartRateListener(){
        val passiveListenerConfig = PassiveListenerConfig.Builder()
            .setDataTypes(setOf(DataType.HEART_RATE_BPM))
            .build()
        // using callback : still works even in background
        val passiveListenerCallback: PassiveListenerCallback = object :
            PassiveListenerCallback {
            override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
                dataPoints.getData(DataType.HEART_RATE_BPM).forEach {
                    GlobalScope.launch {
                        Log.d("PassiveDataService", ">> onNewDataPointsReceived: previous heartRate : ${heartRateFlow.value} heartRate received ${it.value.toInt()}")
                        Log.d("PassiveDataService", ">> onNewDataPointsReceived: metadata : ${it.metadata}")
                        heartRateFlow.emit(it.value.toInt())
                    }
                }
            }
        }
        passiveMonitoringClient!!.setPassiveListenerCallback(
            passiveListenerConfig,
            passiveListenerCallback
        )

    }
}
