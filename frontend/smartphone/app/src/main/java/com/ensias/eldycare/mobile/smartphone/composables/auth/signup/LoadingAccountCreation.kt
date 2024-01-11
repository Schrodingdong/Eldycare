package com.ensias.eldycare.mobile.smartphone.composables.auth.signup

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ensias.eldycare.mobile.smartphone.composables.Screen
import com.ensias.eldycare.mobile.smartphone.data.SignupData
import com.ensias.eldycare.mobile.smartphone.data.model.AuthRegisterModel
import com.ensias.eldycare.mobile.smartphone.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * LOADING PAGE FOR ACCOUNT CREATION
 */
@Composable
fun LoadingAccountCreation(signupData: SignupData, navController: NavController){
    var showErrorCreatingAccountDialog by remember { mutableStateOf(false) }
    Log.d("LoadingAccountCreationPage", "Data to send : \n\t>> " + signupData.toString())

    LaunchedEffect(Unit){
        AuthService().register(
            AuthRegisterModel(
                username = signupData.name,
                email = signupData.email,
                phone = signupData.phone,
                password = signupData.password,
                userType = signupData.userType
            )
        ).let { response ->
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Log.d("LoadingAccountCreationPage", "Response : \n\t>> " + body.toString())
                    withContext(Dispatchers.Main) {
                        delay(1000)
                        navController.navigate(Screen.Login.route)
                    }
                }
            } else {
                showErrorCreatingAccountDialog = true
                Log.e(
                    "LoadingAccountCreationPage",
                    "Response : \n\t>> " + response.errorBody().toString()
                )
            }
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopDecorWithText("HOLD ON ...")
            Column(
                modifier = Modifier
                    .padding(bottom = 56.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Creating your account",
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Button(onClick = {
                    navController.navigate(Screen.Login.route)
                }) {
                    Text(text = "Cancel")
                }
            }
        }
        if(showErrorCreatingAccountDialog){
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.5f)
            ) {
                AlertDialog(
                    onDismissRequest = {
                        navController.navigate(Screen.Login.route)
                    },
                    title = { Text(text = "Error") },
                    text = { Text(text = "Error while creating your account") },
                    confirmButton = {
                        Button(onClick = {
                            showErrorCreatingAccountDialog = false
                            navController.navigate(Screen.Signup.route)
                        }) {
                            Text(text = "OK")
                        }
                    })

            }
        }
    }
}


