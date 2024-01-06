package com.ensias.eldycare.mobile.smartphone.data.api_model

import com.ensias.eldycare.mobile.smartphone.UserType

data class ElderContactsModel(
    val email: String,
    val username: String,
    val phone: String,
    val userType: UserType
)
