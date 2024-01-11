package com.ensias.eldycare.mobile.smartphone.service

import android.util.Log
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.data.Connection
import com.ensias.eldycare.mobile.smartphone.data.api_model.ElderContactsModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ConnectionService {
    companion object {
        var connectionList: List<Connection> = emptyList()
        val authClient = ApiClient().authApi
        suspend fun loadConnectionList(): List<Connection> {
            var connectionList: List<ElderContactsModel>? = emptyList()
            runBlocking {
                connectionList = authClient.getElderContacts().body()
            }
            connectionList?.let {
                Log.d("ConnectionService", "Loaded connection list : $it")
                val newConnections = it.map {
                    Connection(
                        email = it.email,
                        name = it.username,
                        phone = it.phone,
                        lastAlert = null // TODO
                    )
                }
                Log.d("ConnectionService", "Mapped and returning Connection list : $newConnections")
                return newConnections
            }
            return emptyList()
        }
    }
}