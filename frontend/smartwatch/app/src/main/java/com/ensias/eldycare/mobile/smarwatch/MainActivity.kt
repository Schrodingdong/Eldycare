/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ensias.eldycare.mobile.smarwatch

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerCallback
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.getCapabilities
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Wearable
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val ANOMALY_DETECTION_CAPABILITY_NAME = "anomaly_detection"
private const val ANOMALY_DETECTION_CAPABILITY_PATH = "/anomaly_detection"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val heartRateFlow = MutableStateFlow(0)

        // Launch a coroutine to collect and update the heart rate value
        val healthClient = HealthServices.getClient(this)
        val passiveMonitoringClient = healthClient.passiveMonitoringClient

        // Request permission to access body sensors
        val requiredPermission = android.Manifest.permission.BODY_SENSORS
        ActivityCompat.requestPermissions(this, arrayOf(requiredPermission), 0)

        // Proceed with accessing health data
        if (ContextCompat.checkSelfPermission(this, requiredPermission) == PackageManager.PERMISSION_GRANTED) {
            lifecycleScope.launch {
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
                    val passiveListenerCallback: PassiveListenerCallback = object : PassiveListenerCallback {
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











        // accesing HardwareData
        // sensors
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val linAccelerometerSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        val gyroscopeSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        val accelerometerSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magneticSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val gravitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        // their values
        val linAccelerometerValues = FloatArray(3)
        val gyroscopeValues = FloatArray(3)
        val accelerometerValues = FloatArray(3)
        val magneticValues = FloatArray(3)
        val gravityValues = FloatArray(3)
        // calibration : TODO WE WAIT FOR A BIT BEFORE USING : WE TELL THE USER TO STAY STILL
        val baselineOrientation = FloatArray(3) // will be used to determin the new orientation


        // get accelerometer data
        val listener = VerticalAccelerationSchenanagans()
        sensorManager.registerListener(
//            GyroscopeSensorListener(gyroscopeValues, baselineOrientation),
            listener,
            gyroscopeSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
//            MagneticSensorListener(magneticValues),
            listener,
            magneticSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
//        sensorManager.registerListener(
//            AccelerometerSensorListener(accelerometerValues, baselineOrientation, magneticValues),
//            accelerometerSensor,
//            SensorManager.SENSOR_DELAY_NORMAL
//        )
//        sensorManager.registerListener(
//            GravitySensorListener(gravityValues),
//            gravitySensor,
//            SensorManager.SENSOR_DELAY_NORMAL
//        )
        sensorManager.registerListener(
//            LinAccelerometerListener(
//                linAccelerometerValues,
//                orientation = baselineOrientation
//            ),
            listener,
            linAccelerometerSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )














        // send message to handheld
        Wearable.getMessageClient(this).sendMessage(
            "default_node",
            "/message_path",
            "Hello from the Wear OS app!".toByteArray()
        ).apply {
            addOnSuccessListener {
                Log.d("MainActivity", "Message sent successfully")
            }
            addOnFailureListener {
                Log.d("MainActivity", "Message failed to send")
            }
        }

        setContent {
            HeartRateScreen(heartRateFlow.collectAsState().value)
        }
    }
}

class LinAccelerometerListener(
    private val values: FloatArray,
    val orientation: FloatArray,
) : SensorEventListener {
    override fun onSensorChanged(event: SensorEvent) {
        var x = event.values[0]
        var y = event.values[1]
        var z = event.values[2]
        // remove noise
        val eps = 0.05f
        if (x < eps && x > -eps) x = 0f
        if (y < eps && y > -eps) y = 0f
        if (z < eps && z > -eps) z = 0f
        // assign and low pass
        val alpha = 0.8f
        values[0] = alpha * x + (1 - alpha) * values[0]
        values[1] = alpha * y + (1 - alpha) * values[1]
        values[2] = alpha * z + (1 - alpha) * values[2]
        Log.d("AccelerotmeterListener", "onSensorChanged: Linear Acceleration :  ${values.contentToString()}")


        // use the rotation matrix to get vertical acceleration
        Log.d("AccelerotmeterListener", "Device orientation : ${orientation.contentToString()}")
        val verticalLinearAcceleration = FloatArray(3)
        val xRotation = orientation[1]
        val yRotation = orientation[2]
        verticalLinearAcceleration[0] = values[0] * cos(xRotation) * cos(yRotation) + values[1] * sin(yRotation) + values[2] * sin(xRotation) * cos(yRotation)
        verticalLinearAcceleration[1] = values[0] * cos(xRotation) * sin(yRotation) - values[1] * cos(yRotation) + values[2] * sin(xRotation) * sin(yRotation)
        verticalLinearAcceleration[2] = values[0] * sin(xRotation) - values[2] * cos(xRotation)
        Log.d("AccelerotmeterListener", "verticalAcceleration : ${verticalLinearAcceleration.contentToString()}")
        Log.d("AccelerotmeterListener", "verticalAcceleration magnitude : ${sqrt(verticalLinearAcceleration[0] * verticalLinearAcceleration[0] + verticalLinearAcceleration[1] * verticalLinearAcceleration[1] + verticalLinearAcceleration[2] * verticalLinearAcceleration[2])}")
        Log.d("AccelerotmeterListener", "linear acceleration magnitude : ${sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])}")
        Log.d("AccelerotmeterListener", "===============================================================")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("AccelerotmeterListener", "onAccuracyChanged: $accuracy")
    }
}

class GyroscopeSensorListener(private val values: FloatArray, private val baselineOrientation: FloatArray) : SensorEventListener {
    // Create a constant to convert nanoseconds to seconds.
    private val NS2S = 1.0f / 1000000000.0f
    private val deltaRotationVector = FloatArray(4) { 0f }
    private var timestamp: Long = 0L
    private val EPSILON = 0.1f
    private var angleX: Double = 0.0
    private var angleY: Double = 0.0
    private var angleZ: Double = 0.0

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
            if (timestamp != 0L) {
                val dT = (event.timestamp - timestamp) * NS2S
                val omegaX = event.values[0]
                val omegaY = event.values[1]
                val omegaZ = event.values[2]

                // Integration to estimate angles
                angleX += omegaX * dT
                angleY += omegaY * dT
                angleZ += omegaZ * dT

                // Output the angles of rotation
                Log.d("GyroscopeSensorListener","Angle X: $angleX radians, Angle Y: $angleY radians, Angle Z: $angleZ radians")

                // update baseline orientation
                baselineOrientation[0] = baselineOrientation[0] + angleX.toFloat()
                baselineOrientation[1] = baselineOrientation[1] + angleY.toFloat()
                baselineOrientation[2] = baselineOrientation[2] + angleZ.toFloat()
            }
            timestamp = event.timestamp
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }
}

class MagneticSensorListener(private val values: FloatArray) : SensorEventListener {
    override fun onSensorChanged(event: SensorEvent?) {
        var x = event?.values?.get(0)!!
        var y = event?.values?.get(1)!!
        var z = event?.values?.get(2)!!
        // remove noise
        val eps = 0.05f
        if (x < eps && x > -eps) x = 0f
        if (y < eps && y > -eps) y = 0f
        if (z < eps && z > -eps) z = 0f
        // assign and low pass
        val alpha = 0.8f
        values[0] = alpha * x + (1 - alpha) * values[0]
        values[1] = alpha * y + (1 - alpha) * values[1]
        values[2] = alpha * z + (1 - alpha) * values[2]
        Log.d("MagneticSensorListener", "onSensorChanged: [x: ${String.format("%.2f", values[0])}, y: ${String.format("%.2f", values[1])}, z: ${String.format("%.2f", values[2])}]")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("MagneticSensorListener", "onAccuracyChanged: $accuracy")
    }
}

class AccelerometerSensorListener(
    private val values: FloatArray,
    private val baselineOrientation: FloatArray,
    private val magneticValues: FloatArray
) : SensorEventListener {
    var isCalibrated = false
    override fun onSensorChanged(event: SensorEvent?) {
        var x = event?.values?.get(0)!!
        var y = event?.values?.get(1)!!
        var z = event?.values?.get(2)!!
        // remove noise
        val eps = 0.05f
        if (x < eps && x > -eps) x = 0f
        if (y < eps && y > -eps) y = 0f
        if (z < eps && z > -eps) z = 0f
        // assign and low pass
        val oldValues = values.clone()
        val alpha = 0.8f
        values[0] = alpha * x + (1 - alpha) * values[0]
        values[1] = alpha * y + (1 - alpha) * values[1]
        values[2] = alpha * z + (1 - alpha) * values[2]

        // wait for 5 seconds as long as the phone is not moving
        val threshold = 0.1f
        while(
            !isCalibrated &&
            (values[0] - oldValues[0] < threshold && values[0] - oldValues[0] > -threshold) &&
            (values[1] - oldValues[1] < threshold && values[1] - oldValues[1] > -threshold) &&
            (values[2] - oldValues[2] < threshold && values[2] - oldValues[2] > -threshold)
        ){
            Thread.sleep(1000)
        }

        if(!isCalibrated){
            // calculate the rotation matrix
            val R = FloatArray(9) { 0f }
            SensorManager.getRotationMatrix(R, null, values, magneticValues)
            // calculate the orientation
            val orientation = FloatArray(3) { 0f }
            SensorManager.getOrientation(R, orientation)
            // copy the orientation
            baselineOrientation[0] = orientation[0]
            baselineOrientation[1] = orientation[1]
            baselineOrientation[2] = orientation[2]
            isCalibrated = true
            Log.d("AccelerometerSensorListener", "onSensorChanged: calibrated successfully !! ${baselineOrientation[0]} ${baselineOrientation[1]} ${baselineOrientation[2]}")
        }
        Log.d("AccelerometerSensorListener", "onSensorChanged: [x: ${String.format("%.2f", values[0])}, y: ${String.format("%.2f", values[1])}, z: ${String.format("%.2f", values[2])}]")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("AccelerometerSensorListener", "onAccuracyChanged: $accuracy")
    }
}

class GravitySensorListener(private val values: FloatArray) : SensorEventListener {
    override fun onSensorChanged(event: SensorEvent?) {
        var x = event?.values?.get(0)!!
        var y = event?.values?.get(1)!!
        var z = event?.values?.get(2)!!
        // remove noise
        val eps = 0.05f
        if (x < eps && x > -eps) x = 0f
        if (y < eps && y > -eps) y = 0f
        if (z < eps && z > -eps) z = 0f
        // assign and low pass
        val alpha = 0.8f
        values[0] = alpha * x + (1 - alpha) * values[0]
        values[1] = alpha * y + (1 - alpha) * values[1]
        values[2] = alpha * z + (1 - alpha) * values[2]
        Log.d("GravitySensorListener", "onSensorChanged: [x: ${String.format("%.2f", values[0])}, y: ${String.format("%.2f", values[1])}, z: ${String.format("%.2f", values[2])}]")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("GravitySensorListener", "onAccuracyChanged: $accuracy")
    }
}





@Composable
fun HeartRateScreen(heartRate: Int) {
    val mContext = LocalContext.current


    LaunchedEffect(key1 = null, block =  {
        GlobalScope.launch {
            // testing message sending
            val capabilityInfo: CapabilityInfo = Tasks.await(
                Wearable.getCapabilityClient(mContext)
                    .getCapability(
                        ANOMALY_DETECTION_CAPABILITY_NAME,
                        CapabilityClient.FILTER_REACHABLE
                    )
            )
            // capabilityInfo has the reachable nodes with the transcription capability
            Log.d("MainActivity", "setupVoiceTranscription: ${capabilityInfo.nodes.size}")

            // returns the closest node to us (for processing), or null if there are none
            val nodes = capabilityInfo.nodes
            val nodeId = nodes.firstOrNull { it.isNearby }?.id ?: nodes.firstOrNull()?.id

            if(nodeId == null){
                Log.d("MainActivity", "setupVoiceTranscription: no node found")
                return@launch
            }

            Log.d("MainActivity", "setupVoiceTranscription: node found : $nodeId")
            Wearable.getMessageClient(mContext).sendMessage(
                nodeId,
                ANOMALY_DETECTION_CAPABILITY_PATH,
                "Hello from the Wear OS app!".toByteArray()
            ).apply {
                addOnSuccessListener {
                    Log.d("MainActivity", "Message sent successfully")
                }
                addOnFailureListener {
                    Log.d("MainActivity", "Message failed to send")
                }
            }

        }
    })


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Heart Rate",
            style = MaterialTheme.typography.title3,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedEcgPulse()
        Spacer(modifier = Modifier.width(16.dp))
        if(heartRate <= 0){
            Text(
                text = "Calculating...",
                style = MaterialTheme.typography.body1,
                color = Color.Gray
            )
        } else {
            Text(
                text = "$heartRate BPM",
                style = MaterialTheme.typography.body1
            )
        }

    }
}

@Composable
fun AnimatedEcgPulse() {
    val transition = rememberInfiniteTransition()
    val visibility by transition.animateFloat(
        initialValue = .5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = " ecg pulse animation"
    )
    Icon(
        painter = painterResource(id = com.ensias.eldycare.mobile.R.drawable.ecg_vector),
        contentDescription = null,
        tint = MaterialTheme.colors.primaryVariant,
        modifier = Modifier.alpha(visibility)
    )
}


