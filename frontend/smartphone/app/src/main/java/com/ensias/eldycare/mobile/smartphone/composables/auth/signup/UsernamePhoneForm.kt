package com.ensias.eldycare.mobile.smartphone.composables.auth.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ensias.eldycare.mobile.smartphone.R
import com.ensias.eldycare.mobile.smartphone.data.SignupData

/**
 * SIGNUP PAGE
 */
@Composable
fun UsernamePhoneForm(
    signupData: SignupData,
    signupStep: Int,
    onSignupDataChange: (SignupData) -> Unit,
    onSignupStepChange: (Int) -> Unit,
    navController: NavController
){
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopDecorWithText("WHO ARE\nYOU")
            Column(
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                usernamePhoneForm_Composable(signupData, signupStep, onSignupDataChange, onSignupStepChange)
                LoginAlternative(navController)
            }
        }
    }
}
@Composable
fun usernamePhoneForm_Composable(signupData: SignupData, signupStep: Int, onSignupDataChange: (SignupData) -> Unit, onSignupStepChange: (Int) -> Unit){
    var isPhoneValid by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.signup_username_phone_illustration),
            contentDescription = "signup",
            modifier= Modifier.width(250.dp)
        )
        Column (modifier = Modifier.fillMaxWidth()){
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = signupData.name,
                onValueChange = { onSignupDataChange(signupData.copy(name = it)) },
                label = { Text("Name") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = signupData.phone,
                onValueChange = {
                    onSignupDataChange(signupData.copy(phone= it))
                    isPhoneValid = isPhoneNumber(it)
                },
                label = { Text("Phone Number") }
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        if (isPhoneValid){
            Button(onClick = { onSignupStepChange(signupStep + 1) }){
                Text("Next")
            }
        } else {
            Button(onClick = {}, enabled = false){
                Text("Next")
            }
        }
    }
}

fun isPhoneNumber(input: String): Boolean {
    // Define a regular expression for a simple phone number pattern
    val phoneNumberRegex = Regex("^\\+?[0-9.-]+\$")

    // Check if the input string matches the phone number pattern
    return phoneNumberRegex.matches(input)
}
