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
package com.example.android.wearable.composestarter.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerCallback
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.getCapabilities
import androidx.health.services.client.setPassiveListenerService
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.android.wearable.composestarter.R
import com.example.android.wearable.composestarter.presentation.theme.WearAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
        super.onCreate(savedInstanceState)
        setContent {
            HeartRateScreen(heartRateFlow.collectAsState().value)
        }
    }
}

@Composable
fun HeartRateScreen(heartRate: Int) {
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
        painter = painterResource(id = R.drawable.ecg_vector),
        contentDescription = null,
        tint = MaterialTheme.colors.primaryVariant,
        modifier = Modifier.alpha(visibility)
    )
}
