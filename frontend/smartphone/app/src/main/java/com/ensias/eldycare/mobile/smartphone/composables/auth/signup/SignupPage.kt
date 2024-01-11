package com.ensias.eldycare.mobile.smartphone.composables.auth.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.ensias.eldycare.mobile.smartphone.UserType
import com.ensias.eldycare.mobile.smartphone.composables.Screen
import com.ensias.eldycare.mobile.smartphone.data.SignupData

@Composable
fun SignupPage(navController: NavController) {
    var signupStep by remember { mutableStateOf(1) }
    var signupData by remember { mutableStateOf(
        SignupData(
            name = "",
            email = "",
            password = "",
            userType = UserType.RELATIVE,
            phone = ""
        )
    )}

    when(signupStep){
        1 -> UsernamePhoneForm(signupData, signupStep, { signupData = it }, { signupStep = it }, navController)
        2 -> MailPasswordForm(signupData, signupStep, { signupData = it }, { signupStep = it }, navController)
        3 -> UserChoiceForm(signupData, signupStep, { signupData = it }, { signupStep = it }, navController)
        4 -> LoadingAccountCreation(signupData, navController)
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
