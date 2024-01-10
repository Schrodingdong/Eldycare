package com.ensias.eldycare.mobile.smartphone.api

import com.ensias.eldycare.mobile.smartphone.UserType
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JWTInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${ApiClient.jwt}")  // Assuming JWT is stored in ApiClient
            .build()
        return chain.proceed(request)
    }
}
object AppOkHttpClient {
    val client = OkHttpClient.Builder()
        .addInterceptor(JWTInterceptor())
        .build()
}
object RetrofitClient {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(AppOkHttpClient.client)
            .build()
    }
}

class ApiClient{
    companion object {
        var jwt: String = ""
        var userType: UserType = UserType.RELATIVE
        var email: String = ""
        // TODO : change with the ip of your machine (localhost wont work)
        val HOSTNAME = "192.168.11.173"
        val PORT = "8888"
        val BASE_URL = "http://$HOSTNAME:$PORT/"
        val gson = Gson()
    }
    val authApi: AuthApi = RetrofitClient.retrofit.create(AuthApi::class.java)
}
