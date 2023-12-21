package com.ensias.eldycare.mobile.smartphone.service

import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.data.api_model.LoginResponseModel
import com.ensias.eldycare.mobile.smartphone.data.api_model.RegisterResponseModel
import com.ensias.eldycare.mobile.smartphone.data.model.AuthLoginModel
import com.ensias.eldycare.mobile.smartphone.data.model.AuthRegisterModel
import okhttp3.ResponseBody
import retrofit2.Response

class AuthService {
    suspend fun register(authRegisterModel: AuthRegisterModel): Response<RegisterResponseModel> {
        return ApiClient().authApi.register(authRegisterModel).body()?.let {
            Response.success(it)
        } ?: Response.error(400, ResponseBody.create(null, "Error"))
}
    suspend fun login(authLoginModel: AuthLoginModel): Response<LoginResponseModel> {
        return ApiClient().authApi.login(authLoginModel).body()?.let {
            Response.success(it)
        } ?: Response.error(400, ResponseBody.create(null, "Error"))

    }
}