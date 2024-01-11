package com.ensias.eldycare.mobile.smartphone.service

import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.data.Connection
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConnectionService {
    companion object {
        val instance = ConnectionService()
        var connectionList: List<Connection>? = null
    }
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun loadConnectionList(onConnectionListChange : (List<Connection>) -> Unit = {}) {
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
                onConnectionListChange(newConnections)
//                connectionList = newConnections
            }
        }
    }
}