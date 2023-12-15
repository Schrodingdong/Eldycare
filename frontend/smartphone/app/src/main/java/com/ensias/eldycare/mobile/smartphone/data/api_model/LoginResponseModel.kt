package com.ensias.eldycare.mobile.smartphone.data.api_model

import com.ensias.eldycare.mobile.smartphone.UserType

data class LoginResponseModel(
    val jwt: String,
    val userType: UserType,
    val message: String
)