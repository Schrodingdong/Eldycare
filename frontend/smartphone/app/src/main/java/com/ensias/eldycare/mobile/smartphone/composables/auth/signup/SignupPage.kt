package com.ensias.eldycare.mobile.smartphone.composables.auth.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.ensias.eldycare.mobile.smartphone.UserType
import com.ensias.eldycare.mobile.smartphone.data.SignupData

@Composable
fun SignupPage(navController: NavController) {
    var signupStep by remember { mutableStateOf(1) }
    var signupData by remember { mutableStateOf(
        SignupData(
            name = "",
            email = "",
            password = "",
            userType = UserType.RELATIVE
        )
    )}

    when(signupStep){
        1 -> SignUpMainForm(signupData, signupStep, { signupData = it }, { signupStep = it }, navController)
        2 -> UserTypeChoicePage(signupData, signupStep, { signupData = it }, { signupStep = it }, navController)
        3 -> LoadingAccountCreationPage(signupData, navController)
    }
}