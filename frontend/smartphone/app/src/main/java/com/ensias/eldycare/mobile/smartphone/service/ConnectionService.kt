package com.ensias.eldycare.mobile.smartphone.service

import android.util.Log
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.data.Connection
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConnectionService {
    companion object {
        val instance = ConnectionService()
        var connectionList: List<Connection>? = null
        @OptIn(DelicateCoroutinesApi::class)
        fun loadConnectionList() {
            GlobalScope.launch {
                ApiClient().authApi.getElderContacts().body()?.let {
                    val newConnections = it.map {
                        Connection(
                            email = it.email,
                            name = it.username,
                            phone = it.phone,
                            lastAlert = null // TODO
                        )
                    }
                    // set the list to trigger composition
                    connectionList = newConnections
                }

            }
        }
    }
}