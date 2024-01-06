package com.ensias.eldycare.mobile.smartphone.composables.auth.signup

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ensias.eldycare.mobile.smartphone.R
import com.ensias.eldycare.mobile.smartphone.composables.Screen
import com.ensias.eldycare.mobile.smartphone.data.SignupData

/**
 * SIGNUP PAGE
 */
@Composable
fun SignUpMainForm(signupData: SignupData, signupStep: Int, onSignupDataChange: (SignupData) -> Unit, onSignupStepChange: (Int) -> Unit, navController: NavController){
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopDecorWithText("CREATE AN\nACCOUNT")
            Column(
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp, bottom = 56.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                SignupForm(signupData, signupStep, onSignupDataChange, onSignupStepChange)
                LoginAlternative(navController)
            }
        }
    }
}
@Composable
fun TopDecorWithText(text: String){
    val textElements = text.split('\n')
    Box(
        // make the element vertically centered while layered
        contentAlignment = Alignment.CenterStart,
    ){
        Box {
            Image(painter = painterResource(id = R.drawable.top_decor), contentDescription = "top_decor")
        }
        Column {
            for(t in textElements){
                Text(
                    text = t,
                    modifier = Modifier.padding(start = 32.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                )
            }
        }
    }
}
@Composable
fun SignupForm(signupData: SignupData, signupStep: Int, onSignupDataChange: (SignupData) -> Unit, onSignupStepChange: (Int) -> Unit){
    var confirmPasswordText by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf(false) }
    var isPhoneValid by remember { mutableStateOf(false) }
    fun checkPassword(password: String, confirmPassword: String): Boolean{
        return password == confirmPassword
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.signup_illustration),
            contentDescription = "signup",
            modifier= Modifier.width(275.dp)
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
        if (confirmPassword && isPhoneValid){
            Button(onClick = {
                onSignupStepChange(signupStep + 1)
                Log.d("signupData", "My signup data : \n\t>> "+ signupData.toString())
            }
            ){
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
@Composable
fun LoginAlternative(navController: NavController) {
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Already have an account?")
            OutlinedButton(onClick = {
                navController.navigate(Screen.Login.route)
            }) {
                Text("login")
            }
        }
    }
}
