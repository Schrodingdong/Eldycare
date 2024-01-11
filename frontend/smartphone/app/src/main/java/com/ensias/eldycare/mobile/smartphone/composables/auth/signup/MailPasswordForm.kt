package com.ensias.eldycare.mobile.smartphone.composables.auth.signup

import android.util.Log
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

@Composable
fun MailPasswordForm(
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
            TopDecorWithText("YOUR\nCREDENTIALS")
            Column(
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                MailPasswordForm_Composable(signupData, signupStep, onSignupDataChange, onSignupStepChange)
                LoginAlternative(navController)
            }
        }
    }
}

@Composable
fun MailPasswordForm_Composable(signupData: SignupData, signupStep: Int, onSignupDataChange: (SignupData) -> Unit, onSignupStepChange: (Int) -> Unit){
    var confirmPasswordText by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf(false) }
    fun checkPassword(password: String, confirmPassword: String): Boolean{
        return password == confirmPassword
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.signup_email_password_illustration),
            contentDescription = "signup",
            modifier= Modifier.width(250.dp)
        )
        Column (modifier = Modifier.fillMaxWidth()){
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = signupData.email,
                onValueChange = { onSignupDataChange(signupData.copy(email = it)) },
                label = { Text("Email") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = signupData.password,
                onValueChange = {  onSignupDataChange(signupData.copy(password = it))},
                label = { Text("Password") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = confirmPasswordText,
                onValueChange = {
                    confirmPasswordText = it
                    confirmPassword = checkPassword(signupData.password, confirmPasswordText)
                    Log.d("confirmPassword", confirmPassword.toString())
                },
                label = { Text("Confirm Password") }
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        if (confirmPassword){
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
