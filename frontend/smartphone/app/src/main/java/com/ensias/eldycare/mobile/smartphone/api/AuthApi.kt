package com.ensias.eldycare.mobile.smartphone.api

import com.ensias.eldycare.mobile.smartphone.data.model.NotificationModel
import com.ensias.eldycare.mobile.smartphone.data.api_model.LoginResponseModel
import com.ensias.eldycare.mobile.smartphone.data.api_model.NotificationResponse
import com.ensias.eldycare.mobile.smartphone.data.api_model.RegisterResponseModel
import com.ensias.eldycare.mobile.smartphone.data.model.AuthLoginModel
import com.ensias.eldycare.mobile.smartphone.data.model.AuthRegisterModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/login")
    suspend fun login(@Body authLoginModel: AuthLoginModel) : Response<LoginResponseModel>
    @POST("/auth/register")
    suspend fun register(@Body authRegisterModel: AuthRegisterModel) : Response<RegisterResponseModel>
    @POST("/notification/send")
    suspend fun sendNotification(@Body notificationModel: NotificationModel) : Response<NotificationResponse>
    @GET("/users/get/elder-contacts")
    suspend fun getElderContacts(@Header("User-Email") userEmail: String) : Response<List<String>>
}