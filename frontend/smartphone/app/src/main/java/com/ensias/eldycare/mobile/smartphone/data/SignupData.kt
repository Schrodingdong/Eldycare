package com.ensias.eldycare.mobile.smartphone.data

import com.ensias.eldycare.mobile.smartphone.UserType

data class SignupData(
    var name: String,
    var email: String,
    var phone: String,
    var password: String,
    var userType: UserType,
)
