package com.ensias.eldycare.mobile.smartphone.data.model

import com.ensias.eldycare.mobile.smartphone.UserType

data class AuthRegisterModel(
    val email: String,
    val password: String,
    val username: String,
    val userType: UserType
)