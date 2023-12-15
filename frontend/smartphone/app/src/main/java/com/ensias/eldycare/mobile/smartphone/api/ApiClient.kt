package com.ensias.eldycare.mobile.smartphone.api

import com.ensias.eldycare.mobile.smartphone.UserType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8888/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

class ApiClient{
    companion object {
        var jwt: String = ""
        var userType: UserType = UserType.RELATIVE
    }
    val authApi: AuthApi = RetrofitClient.retrofit.create(AuthApi::class.java)
}
