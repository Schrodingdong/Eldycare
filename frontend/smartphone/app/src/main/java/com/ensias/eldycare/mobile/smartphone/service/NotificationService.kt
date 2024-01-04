package com.ensias.eldycare.mobile.smartphone.service

import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.data.api_model.NotificationResponse
import com.ensias.eldycare.mobile.smartphone.data.model.NotificationModel
import okhttp3.ResponseBody
import retrofit2.Response

class NotificationService {
    // For the elderly
    suspend fun sendNotification(notificationModel: NotificationModel): Response<NotificationResponse>{
        return ApiClient().authApi.sendNotification(notificationModel).body()?.let {
            Response.success(it)
        } ?: Response.error(400, ResponseBody.create(null, "Error"));
    }

    // For the relatives
    fun receiveNotification(notificationModel: String) {
        // TODO : implement this function


    }
}