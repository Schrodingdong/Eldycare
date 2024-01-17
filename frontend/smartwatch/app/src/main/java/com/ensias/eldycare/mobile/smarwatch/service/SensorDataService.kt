package com.ensias.eldycare.mobile.smarwatch.service

import android.app.Activity
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerCallback
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.getCapabilities
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Service to handle data from the sensors
 */
class SensorDataService(
    private val requiredPermission: String,
    private val activity: Activity,
    private val sensorManager: SensorManager
) {
    val heartRateFlow = MutableStateFlow(0)

    fun initialiseSensors(){
        // Request permission to access body sensors
        val requiredPermission = android.Manifest.permission.BODY_SENSORS
        ActivityCompat.requestPermissions(activity, arrayOf(requiredPermission), 0)
        // initialisation
        intializeAccelerometerSensor(sensorManager)
        initializeGyroscopeSensor(sensorManager)
    }

    fun intializeHealthServices(){
        // Launch a coroutine to collect and update the heart rate value
        val healthClient = HealthServices.getClient(activity)
        val passiveMonitoringClient = healthClient.passiveMonitoringClient
        // Proceed with accessing health data
        if (ContextCompat.checkSelfPermission(activity, requiredPermission) == PackageManager.PERMISSION_GRANTED) {
            GlobalScope.launch {
                /* TODO NO SIMULATION*/
                val capabilities = passiveMonitoringClient.getCapabilities()
                val supportsHeartRate = DataType.HEART_RATE_BPM in capabilities.supportedDataTypesPassiveMonitoring
                Log.d("MainActivity", "supportsHeartRate: $supportsHeartRate")
                if(supportsHeartRate){
                    val passiveListenerConfig = PassiveListenerConfig.Builder()
                        .setDataTypes(setOf(DataType.HEART_RATE_BPM))
                        .build()
                    // using a service :
//                    passiveMonitoringClient.setPassiveListenerService(
//                        PassiveDataService::class.java,
//                        passiveListenerConfig
//                    )
                    // using callback : still works even in background
                    val passiveListenerCallback: PassiveListenerCallback = object :
                        PassiveListenerCallback {
                        override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
                            dataPoints.getData(DataType.HEART_RATE_BPM).forEach {
                                runBlocking {
                                    Log.d("PassiveDataService", ">> onNewDataPointsReceived: ${it.value.toInt()}")
                                    Log.d("PassiveDataService", ">> onNewDataPointsReceived: metadata : ${it.metadata}")
                                    Log.d("PassiveDataService", ">> onNewDataPointsReceived: current heartrate : ${heartRateFlow.value}")

                                    heartRateFlow.emit(it.value.toInt())
                                }
                            }
                        }
                    }
                    passiveMonitoringClient.setPassiveListenerCallback(
                        passiveListenerConfig,
                        passiveListenerCallback
                    )
                }
            }
        }

    }

    fun intializeAccelerometerSensor(sensorManager: SensorManager){
        val accelerometerSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensorManager.registerListener(
            AccelerotmeterListener(),
            accelerometerSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

    }

    fun initializeGyroscopeSensor(sensorManager: SensorManager){
        val gyroscopeSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager.registerListener(
            GyroscopeSensorListener(),
            gyroscopeSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }
}

class AccelerotmeterListener : SensorEventListener {
    override fun onSensorChanged(event: SensorEvent) {
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        val alpha: Float = 0.8f

//        // Isolate the force of gravity with the low-pass filter.
//        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
//        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
//        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

        // Remove the gravity contribution with the high-pass filter.
        val linear_acceleration = FloatArray(3) { 0f }
        linear_acceleration[0] = event.values[0] /* - gravity[0] */
        linear_acceleration[1] = event.values[1] /* - gravity[1] */
        linear_acceleration[2] = event.values[2] /* - gravity[2] */

        val threshold = 0.5f
        if (linear_acceleration[0] > threshold || linear_acceleration[1] > threshold || linear_acceleration[2] > threshold) {
            Log.d("AccelerotmeterListener", "onSensorChanged: ${linear_acceleration[0]} ${linear_acceleration[1]} ${linear_acceleration[2]}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("AccelerotmeterListener", "onAccuracyChanged: $accuracy")
    }
}


class GyroscopeSensorListener : SensorEventListener {
    // Create a constant to convert nanoseconds to seconds.
    private val NS2S = 1.0f / 1000000000.0f
    private val deltaRotationVector = FloatArray(4) { 0f }
    private var timestamp: Float = 0f
    private val EPSILON = 0.1f

    override fun onSensorChanged(event: SensorEvent?) {
        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0f && event != null) {
            val dT = (event.timestamp - timestamp) * NS2S
            // Axis of the rotation sample, not normalized yet.
            var axisX: Float = event.values[0]
            var axisY: Float = event.values[1]
            var axisZ: Float = event.values[2]

            // Calculate the angular speed of the sample
            val omegaMagnitude: Float = sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ)

            // Normalize the rotation vector if it's big enough to get the axis
            // (that is, EPSILON should represent your maximum allowable margin of error)
            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude
                axisY /= omegaMagnitude
                axisZ /= omegaMagnitude
            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            val thetaOverTwo: Float = omegaMagnitude * dT / 2.0f
            val sinThetaOverTwo: Float = sin(thetaOverTwo)
            val cosThetaOverTwo: Float = cos(thetaOverTwo)
            deltaRotationVector[0] = sinThetaOverTwo * axisX
            deltaRotationVector[1] = sinThetaOverTwo * axisY
            deltaRotationVector[2] = sinThetaOverTwo * axisZ
            deltaRotationVector[3] = cosThetaOverTwo
        }
        timestamp = event?.timestamp?.toFloat() ?: 0f
        val deltaRotationMatrix = FloatArray(9) { 0f }
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
        // User code should concatenate the delta rotation we computed with the current rotation
        // in order to get the updated rotation.
        // rotationCurrent = rotationCurrent * deltaRotationMatrix;
        // show the rotation matrix
        Log.d("MainActivity", "onSensorChanged: ${deltaRotationVector[0]} ${deltaRotationVector[1]} ${deltaRotationVector[2]} ${deltaRotationVector[3]}")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("MainActivity", "onAccuracyChanged: $accuracy")
    }
}
