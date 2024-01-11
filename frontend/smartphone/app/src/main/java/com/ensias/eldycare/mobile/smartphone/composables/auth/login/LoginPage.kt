package com.ensias.eldycare.mobile.smartphone.composables.auth.login

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.ensias.eldycare.mobile.smartphone.R
import com.ensias.eldycare.mobile.smartphone.UserType
import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.composables.Screen
import com.ensias.eldycare.mobile.smartphone.composables.auth.signup.TopDecorWithText
import com.ensias.eldycare.mobile.smartphone.data.LoginData
import com.ensias.eldycare.mobile.smartphone.data.model.AuthLoginModel
import com.ensias.eldycare.mobile.smartphone.service.AuthService
import kotlinx.coroutines.delay

/**
 * LOGIN PAGE
 */
@Composable
fun LoginPage(navController: NavController) {
    var loginButtonClicked by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var loginData by remember { mutableStateOf(
        LoginData(
            email = "",
            password = ""
        )
    )}

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopDecorWithText("LOGIN")
            Column(
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LoginForm(loginData, { loginData = it }, navController, { loginButtonClicked = it ; showDialog = it})
                SignupAlternative(navController)
            }
        }
        if (showDialog){
            Surface(
                color = Color.Black.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxSize()
            ){
                AuthDialog(
                    message = "Logging in...",
                    navController = navController,
                    loginData = loginData,
                    onShowDialogChange = { showDialog = it },
                )
            }
        }
    }
}
@Composable
fun LoginForm(loginData: LoginData, onLoginDataChange: (LoginData) -> Unit, navController: NavController, onloginButtonClicked: (Boolean) -> Unit){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_illustration),
            contentDescription = "signup",
            modifier = Modifier.width(275.dp)
        )
        Column (modifier = Modifier.fillMaxWidth()){
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = loginData.email,
                onValueChange = {
                    onLoginDataChange(loginData.copy(email = it))
                },
                label = { Text("Email") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = loginData.password,
                onValueChange = {
                    onLoginDataChange(loginData.copy(password = it))
                },
                label = { Text("Password") }
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = {
            onloginButtonClicked(true)
            Log.d("LOGIN", "Clicked : ")
        }){
            Text("Login")
        }
    }
}



enum class DialogState {
    LOADING,
    ERROR,
    SUCCESS,
    NONE
}

@Composable
fun AuthDialog(message: String, navController: NavController, loginData: LoginData, onShowDialogChange: (Boolean) -> Unit){
    var dialogState by remember { mutableStateOf(DialogState.LOADING) }

    LaunchedEffect(Unit){
        AuthService().login(
            AuthLoginModel(
                email = loginData.email,
                password = loginData.password
            )
        ).let {
            delay(2000L)
            if(!it.isSuccessful){
                dialogState = DialogState.ERROR
                Log.d("LOGIN", "Error: ${it.code()}")
            }
            Log.d("LOGIN", "Success: ${loginData.email} ${it.body()?.jwt}, ${it.body()?.userType}")
            ApiClient.userType = it.body()?.userType?: UserType.ELDERLY
            ApiClient.email = loginData.email
            ApiClient.jwt = it.body()?.jwt?: ""
            if (ApiClient.jwt == ""){
                dialogState = DialogState.ERROR
                Log.d("LOGIN", "FAIL !!")
            } else {
                onShowDialogChange(false)

                // navigate to Main page based on user type
                if (it.body()?.userType == UserType.RELATIVE){
                    Log.d("LOGIN", "Success: ${it.body()?.jwt}, ${it.body()?.userType}")
                    navController.navigate(Screen.RelativeHomePage.route)
                }
                else if (it.body()?.userType == UserType.ELDERLY){
                    Log.d("LOGIN", "Success: ${it.body()?.jwt}, ${it.body()?.userType}")
                    navController.navigate(Screen.ElderHomePage.route)
                }
            }
        }
    }

    Dialog(onDismissRequest = { /* Dismiss the dialog */ }){
        if (dialogState == DialogState.ERROR){
            AlertDialog(
                onDismissRequest = {
                    onShowDialogChange(false)
                },
                title = { Text("Error") },
                text = { Text("Invalid email or password") },
                confirmButton = {
                    Button(
                        onClick = {
                            onShowDialogChange(false)
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        } else if(dialogState == DialogState.LOADING) {
            Card (
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(75.dp)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        strokeWidth = 4.dp
                    )
                    Text(
                        text = message,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}


@Composable
fun SignupAlternative(navController: NavController) {
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Don't have an account?")
            OutlinedButton(onClick = {
                navController.navigate(Screen.Signup.route)
            }) {
                Text("Sign Up")
            }
        }
    }
}
