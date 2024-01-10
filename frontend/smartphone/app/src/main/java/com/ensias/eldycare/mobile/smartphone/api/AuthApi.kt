package com.ensias.eldycare.mobile.smartphone.api

import com.ensias.eldycare.mobile.smartphone.data.api_model.ElderContactAddResponseModel
import com.ensias.eldycare.mobile.smartphone.data.api_model.ElderContactsModel
import com.ensias.eldycare.mobile.smartphone.data.model.NotificationModel
import com.ensias.eldycare.mobile.smartphone.data.api_model.LoginResponseModel
import com.ensias.eldycare.mobile.smartphone.data.api_model.NotificationResponse
import com.ensias.eldycare.mobile.smartphone.data.api_model.RegisterResponseModel
import com.ensias.eldycare.mobile.smartphone.data.api_model.ReminderRequest
import com.ensias.eldycare.mobile.smartphone.data.api_model.ReminderResponse
import com.ensias.eldycare.mobile.smartphone.data.model.AuthLoginModel
import com.ensias.eldycare.mobile.smartphone.data.model.AuthRegisterModel
import com.ensias.eldycare.mobile.smartphone.data.model.ReminderModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {
    @POST("/auth/login")
    suspend fun login(@Body authLoginModel: AuthLoginModel) : Response<LoginResponseModel>
    @POST("/auth/register")
    suspend fun register(@Body authRegisterModel: AuthRegisterModel) : Response<RegisterResponseModel>
    @POST("/notification/send")
    suspend fun sendNotification(@Body notificationModel: NotificationModel) : Response<NotificationResponse>
    @GET("/users/get/elder-contacts")
    suspend fun getElderContacts() : Response<List<ElderContactsModel>>
    @PUT("/users/add/elder-contact/{email}")
    suspend fun addElderContact(@Path("email") email: String) : Response<ElderContactAddResponseModel>
    @POST("/reminder/send")
    suspend fun sendReminder(@Body reminderRequest: ReminderRequest) : Response<ReminderResponse>
}