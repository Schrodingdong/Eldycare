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
import com.ensias.eldycare.mobile.smarwatch.service.HandheldMessageClient
import com.ensias.eldycare.mobile.smarwatch.service.HealthDataSensorService
import com.ensias.eldycare.mobile.smarwatch.service.SensorListenerFallDetectorService
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val ANOMALY_DETECTION_CAPABILITY_NAME = "anomaly_detection"
private const val ANOMALY_DETECTION_CAPABILITY_PATH = "/anomaly_detection"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HealthDataSensorService(this)


        // accesing HardwareData
        // sensors
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val linAccelerometerSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        val gyroscopeSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        val magneticSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val gravitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        // listener
        val listener = SensorListenerFallDetectorService(
            HandheldMessageClient(Wearable.getMessageClient(this))
        )

        sensorManager.registerListener(
            listener,
            gyroscopeSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            listener,
            magneticSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            listener,
            gravitySensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
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
            HeartRateScreen(HealthDataSensorService.heartRateFlow.collectAsState().value)
        }
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


